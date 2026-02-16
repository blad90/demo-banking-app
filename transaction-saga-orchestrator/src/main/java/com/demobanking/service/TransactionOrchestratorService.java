package com.demobanking.service;

import com.demobanking.entity.TransactionSagaState;
import com.demobanking.entity.TransactionSagaStatus;
import com.demobanking.entity.TransactionSagaStep;
import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.AccountValidatedEvent;
import com.demobanking.events.Accounts.ValidateAccountCommand;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.TransactionCreatedEvent;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.repository.TransacSagaStateRepository;
import com.demobanking.request.TransactionRequest;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionOrchestratorService implements ITransactionOrchestratorService {

    private final KafkaTemplate<String, Message> template;
    private final TransacSagaStateRepository sagaStateRepository;

    public TransactionSagaState initiateTransaction(TransactionRequest transactionRequest){
        TransactionSagaState transactionSagaState = new TransactionSagaState(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                transactionRequest.getSourceAccountNumber(),
                transactionRequest.getDestinationAccountNumber(),
                transactionRequest.getAmount(),
                transactionRequest.getTransactionType(),
                transactionRequest.getDescription(),
                TransactionSagaStatus.STARTED,
                TransactionSagaStep.CREATE_TRANSACTION
        );
        sagaStateRepository.save(transactionSagaState);
        // Step 1 - Send command to validate origin account
        validateAccount(transactionSagaState.getSagaId(), transactionRequest);

        return transactionSagaState;
    }

    public void validateAccount(String sagaId, TransactionRequest transactionRequest){
        ValidateAccountCommand validateAccountCommand =
                ValidateAccountCommand.newBuilder()
                        .setSagaId(sagaId)
                        .setAccountNumber(transactionRequest.getSourceAccountNumber())
                        .build();
        template.send("VALIDATE_ACCOUNT_CMD", validateAccountCommand);
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
                .setTransactionType(transactionSagaState.getTransactionType())
                .setDescription(transactionSagaState.getTransactionDescription())
                .build();

        template.send("CREATE_TRANSACTION_CMD", createTransactionCommand);
    }

    public void transfer(TransactionSagaState transactionSagaState) {
        TransferCommand transferCommand = TransferCommand.newBuilder()
                .setSagaId(transactionSagaState.getSagaId())
                .setCorrelationId(transactionSagaState.getCorrelationId())
                .setSourceAccountNumber(transactionSagaState.getSourceAccountNumber())
                .setDestinationAccountNumber(transactionSagaState.getDestinationAccountNumber())
                .setAmount(String.valueOf(transactionSagaState.getAmount()))
                .build();
        template.send("TRANSFER_CMD", transferCommand);
    }

    @KafkaListener(
            topics = "ACCOUNT_VALIDATED_EVENTS_TOPIC",
            containerFactory = "accountValidatedEventListenerFactory",
            groupId = "account-service-group")
    public void onAccountValidate(AccountValidatedEvent event) {
        TransactionSagaState transactionSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        transactionSagaState.setCurrentStep(TransactionSagaStep.VALIDATE_ORIGIN_ACCOUNT);
        transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.PROCESSING);
        sagaStateRepository.save(transactionSagaState);

        if(event.getValidated()){
            transactionSagaState.setCurrentStep(TransactionSagaStep.CONFIRM_ORIGIN_ACCOUNT);
            sagaStateRepository.save(transactionSagaState);
            createTransaction(transactionSagaState);

        } else {
            // compensation logic
            IO.println("Compensating... [PENDING LOGIC]");
            transactionSagaState.setCurrentStep(TransactionSagaStep.REJECT_TRANSACTION);
            transactionSagaState.setTransactionSagaStatus(TransactionSagaStatus.FAILED);
            sagaStateRepository.save(transactionSagaState);

            AccountValidatedEvent accountValidatedEvent = AccountValidatedEvent.newBuilder()
                    .setSagaId(transactionSagaState.getSagaId())
                    .setAccountNumber("ACC NOT VALIDATED")
                    .setValidated(false)
                    .build();
            template.send("ACCOUNT_NOT_VALIDATED_TOPIC", accountValidatedEvent);
        }
    }

    @KafkaListener(
            topics = "ACCOUNT_NOT_VALIDATED_TOPIC",
            groupId = "transaction-orchestrator-group")
    public void onAccountNotValidate(AccountValidatedEvent accountValidatedEvent) {
        IO.println("ACCOUNT NOT VALIDATED! : " + accountValidatedEvent);
    }

    @KafkaListener(
            topics = "TRANSACTION_CREATED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onTransactionCreation(TransactionCreatedEvent event) {
        if(event.getTransactionType().equals(TransactionType.TRANSFER)){
            UpdateAccountsBalancesCommand updateAccountsBalancesCommand = UpdateAccountsBalancesCommand.newBuilder()
                    .setSagaId(event.getSagaId())
                    .setSourceAccountNumber(event.getSourceAccountNumber())
                    .setDestinationAccountNumber(event.getDestinationAccountNumber())
                    .setAmount(event.getAmount())
                    .build();
            template.send("UPDATE_ACCOUNTS_BALANCES_CMD", updateAccountsBalancesCommand);
        }
    }

    @KafkaListener(
            topics = "ACCOUNTS_BALANCES_UPDATED_EVENTS_TOPIC",
            groupId = "transaction-orchestrator-group",
            containerFactory = "transactionEventListenerFactory")
    public void onAccountsBalancesUpdated(TransactionCreatedEvent event) {
        IO.println(event);
        IO.println("ACCOUNTS WERE UPDATED!");
    }
}
