package com.demobanking.service;

import com.demobanking.entity.TransactionSagaState;
import com.demobanking.entity.TransactionSagaStatus;
import com.demobanking.entity.TransactionSagaStep;
import com.demobanking.events.Accounts.AccountNotValidatedEvent;
import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.UpdateAccountBalanceCommand;
import com.demobanking.events.Accounts.BalanceOperation;
import com.demobanking.events.Accounts.AccountBalanceUpdatedEvent;
import com.demobanking.events.Accounts.BalanceUpdateFailedEvent;
import com.demobanking.events.Accounts.AccountValidatedEvent;
import com.demobanking.events.Accounts.ValidateAccountCommand;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.TransactionCreatedEvent;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import com.demobanking.events.Transactions.CancelTransactionCommand;
import com.demobanking.events.Transactions.TransactionCancelledEvent;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.repository.TransacSagaStateRepository;
import com.demobanking.request.TransactionRequest;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionOrchestratorService implements ITransactionOrchestratorService {

    private final KafkaTemplate<String, Message> template;
    private final TransacSagaStateRepository sagaStateRepository;

    public String initiateTransaction(TransactionRequest transactionRequest){
        String idempotencyKey = transactionRequest.getIdempotencyKey();
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<TransactionSagaState> existing = sagaStateRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                // Same request seen before (e.g. a client retry after a timeout) - hand back
                // the saga already in flight instead of moving money twice.
                return existing.get().getSagaId();
            }
        }

        String sagaId = UUID.randomUUID().toString();
        TransactionSagaState transactionSagaState = new TransactionSagaState(
                sagaId,
                UUID.randomUUID().toString(),
                transactionRequest.getCustomerId(),
                transactionRequest.getSourceAccountNumber(),
                transactionRequest.getDestinationAccountNumber(),
                transactionRequest.getAmount(),
                transactionRequest.getTransactionType(),
                transactionRequest.getDescription(),
                TransactionSagaStatus.STARTED,
                TransactionSagaStep.CREATE_TRANSACTION,
                idempotencyKey
        );
        sagaStateRepository.save(transactionSagaState);
        // Step 1 - Send command to validate origin account
        validateAccounts(sagaId, transactionRequest);

        return sagaId;
    }

    public TransactionSagaState retrieveSagaStateById(String sagaId){
        return sagaStateRepository.findById(sagaId).orElseThrow();
    }

    public void validateAccounts(String sagaId, TransactionRequest transactionRequest){
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(sagaId).orElseThrow();
        transactionSagaState.setCurrentStep(TransactionSagaStep.VALIDATE_ORIGIN_ACCOUNT);
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.PROCESSING);
        sagaStateRepository.save(transactionSagaState);

        ValidateAccountCommand.Builder validateAccountCommand =
                ValidateAccountCommand.newBuilder()
                        .setSagaId(sagaId)
                        .setAccountNumber(transactionRequest.getSourceAccountNumber());
        // DEBIT/CREDIT transactions only involve one account - no destination to validate.
        if (transactionRequest.getDestinationAccountNumber() != null
                && !transactionRequest.getDestinationAccountNumber().isBlank()) {
            validateAccountCommand.setDestinationAccountNumber(transactionRequest.getDestinationAccountNumber());
        }
        template.send("VALIDATE_ACCOUNT_CMD", validateAccountCommand.build());
    }

    public void createTransaction(TransactionSagaState transactionSagaState){
        if(transactionSagaState.getTransactionType().equals(TransactionType.TRANSFER)){
            transfer(transactionSagaState);
            return;
        }
        CreateTransactionCommand createTransactionCommand = CreateTransactionCommand.newBuilder()
                .setSagaId(transactionSagaState.getSagaId())
                .setAccountNumber(transactionSagaState.getSourceAccountNumber())
                .setCorrelationId(transactionSagaState.getCorrelationId())
                .setCustomerId(String.valueOf(transactionSagaState.getCustomerId()))
                .setTransactionType(transactionSagaState.getTransactionType())
                .setDescription(transactionSagaState.getTransactionDescription())
                .setAmount(String.valueOf(transactionSagaState.getAmount()))
                .build();

        template.send("CREATE_TRANSACTION_CMD", createTransactionCommand);
    }

    public void transfer(TransactionSagaState transactionSagaState) {
        TransferCommand transferCommand = TransferCommand.newBuilder()
                .setSagaId(transactionSagaState.getSagaId())
                .setCorrelationId(transactionSagaState.getCorrelationId())
                .setCustomerId(String.valueOf(transactionSagaState.getCustomerId()))
                .setSourceAccountNumber(transactionSagaState.getSourceAccountNumber())
                .setDestinationAccountNumber(transactionSagaState.getDestinationAccountNumber())
                .setAmount(String.valueOf(transactionSagaState.getAmount()))
                .setDescription(transactionSagaState.getTransactionDescription())
                .build();
        template.send("TRANSFER_CMD", transferCommand);
    }

    @KafkaListener(
            topics = "ACCOUNT_VALIDATED_EVENTS_TOPIC",
            containerFactory = "accountValidatedEventListenerFactory",
            groupId = "account-service-group")
    public void onAccountValidate(byte[] message) throws InvalidProtocolBufferException {
        AccountValidatedEvent event = AccountValidatedEvent.parseFrom(message);
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        // Already moved past this step - a redelivered/duplicate event, ignore it.
        if (transactionSagaState.getCurrentStep() != TransactionSagaStep.VALIDATE_ORIGIN_ACCOUNT) {
            return;
        }

        if(event.getValidated()){
            transactionSagaState.setCurrentStep(TransactionSagaStep.CONFIRM_ORIGIN_ACCOUNT);
            sagaStateRepository.save(transactionSagaState);
            createTransaction(transactionSagaState);
        }
    }

    @KafkaListener(
            topics = "ACCOUNT_NOT_VALIDATED_TOPIC",
            containerFactory = "accountNotValidatedEventListenerFactory",
            groupId = "account-service-group")
    public void onAccountNotValidate(byte[] message) throws InvalidProtocolBufferException {
        AccountNotValidatedEvent accountNotValidatedEvent = AccountNotValidatedEvent.parseFrom(message);
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(accountNotValidatedEvent.getSagaId()).orElseThrow();
        if (isTerminal(transactionSagaState)) {
            return;
        }
        transactionSagaState.setCurrentStep(TransactionSagaStep.REJECT_TRANSACTION);
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.FAILED);
        sagaStateRepository.save(transactionSagaState);
        IO.println("ACCOUNT NOT VALIDATED! : " + accountNotValidatedEvent);
    }

    @KafkaListener(
            topics = "TRANSACTION_CREATED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onTransactionCreation(byte[] message) throws InvalidProtocolBufferException {
        TransactionCreatedEvent event = TransactionCreatedEvent.parseFrom(message);
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        if (transactionSagaState.getCurrentStep() != TransactionSagaStep.CONFIRM_ORIGIN_ACCOUNT) {
            return;
        }
        transactionSagaState.setCurrentStep(TransactionSagaStep.UPDATE_BALANCE);
        sagaStateRepository.save(transactionSagaState);

        if(event.getTransactionType().equals(TransactionType.TRANSFER)){
            UpdateAccountsBalancesCommand updateAccountsBalancesCommand = UpdateAccountsBalancesCommand.newBuilder()
                    .setSagaId(event.getSagaId())
                    .setSourceAccountNumber(event.getSourceAccountNumber())
                    .setDestinationAccountNumber(event.getDestinationAccountNumber())
                    .setAmount(event.getAmount())
                    .build();
            template.send("UPDATE_ACCOUNTS_BALANCES_CMD", updateAccountsBalancesCommand);
        } else {
            BalanceOperation operation = event.getTransactionType() == TransactionType.CREDIT
                    ? BalanceOperation.CREDIT
                    : BalanceOperation.DEBIT;
            UpdateAccountBalanceCommand updateAccountBalanceCommand = UpdateAccountBalanceCommand.newBuilder()
                    .setSagaId(event.getSagaId())
                    .setAccountNumber(event.getSourceAccountNumber())
                    .setOperation(operation)
                    .setAmount(event.getAmount())
                    .build();
            template.send("UPDATE_ACCOUNT_BALANCE_CMD", updateAccountBalanceCommand);
        }
    }

    @KafkaListener(
            topics = "ACCOUNT_BALANCE_UPDATED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onAccountBalanceUpdated(byte[] message) throws InvalidProtocolBufferException {
        AccountBalanceUpdatedEvent event = AccountBalanceUpdatedEvent.parseFrom(message);
        completeIfPending(event.getSagaId());
    }

    @KafkaListener(
            topics = "ACCOUNTS_BALANCES_UPDATED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onAccountsBalancesUpdated(byte[] message) throws InvalidProtocolBufferException {
        TransactionCreatedEvent event = TransactionCreatedEvent.parseFrom(message);
        completeIfPending(event.getSagaId());
    }

    private void completeIfPending(String sagaId) {
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(sagaId).orElseThrow();
        if (transactionSagaState.getCurrentStep() != TransactionSagaStep.UPDATE_BALANCE) {
            return;
        }
        transactionSagaState.setCurrentStep(TransactionSagaStep.COMPLETE_TRANSACTION);
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.COMPLETED);
        sagaStateRepository.save(transactionSagaState);
    }

    @KafkaListener(
            topics = "BALANCE_UPDATE_FAILED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onBalanceUpdateFailed(byte[] message) throws InvalidProtocolBufferException {
        BalanceUpdateFailedEvent event = BalanceUpdateFailedEvent.parseFrom(message);
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        if (transactionSagaState.getCurrentStep() != TransactionSagaStep.UPDATE_BALANCE) {
            return;
        }

        // Compensate: transaction-service already wrote and marked this transaction TRAN_COMPLETED
        // in the step before this one, but the money never actually moved - cancel that record
        // rather than leave a "completed" transaction stranded behind a failed saga.
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.COMPENSATING);
        sagaStateRepository.save(transactionSagaState);

        CancelTransactionCommand cancelTransactionCommand = CancelTransactionCommand.newBuilder()
                .setSagaId(transactionSagaState.getSagaId())
                .setCorrelationId(transactionSagaState.getCorrelationId())
                .setMessage(event.getMessage())
                .build();
        template.send("CANCEL_TRANSACTION_CMD", cancelTransactionCommand);
        IO.println("BALANCE UPDATE FAILED! : " + event.getMessage());
    }

    @KafkaListener(
            topics = "TRANSACTION_CANCELLED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onTransactionCancelled(byte[] message) throws InvalidProtocolBufferException {
        TransactionCancelledEvent event = TransactionCancelledEvent.parseFrom(message);
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        if (transactionSagaState.getTransactionSagaStatus() != TransactionSagaStatus.COMPENSATING) {
            return;
        }
        // Compensation confirmed - the saga is done unwinding, now it can be reported FAILED.
        transactionSagaState.setCurrentStep(TransactionSagaStep.REJECT_TRANSACTION);
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.FAILED);
        sagaStateRepository.save(transactionSagaState);
    }

    private boolean isTerminal(TransactionSagaState transactionSagaState) {
        return transactionSagaState.getTransactionSagaStatus() == TransactionSagaStatus.FAILED
                || transactionSagaState.getTransactionSagaStatus() == TransactionSagaStatus.COMPLETED;
    }
}
