package com.demobanking.service;

import com.demobanking.entity.AccountSagaState;
import com.demobanking.entity.AccountSagaStatus;
import com.demobanking.entity.AccountSagaStep;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.events.Accounts.AccountCreatedEvent;
import com.demobanking.events.Accounts.AccountNotCreatedEvent;
import com.demobanking.events.Users.UserNotValidatedEvent;
import com.demobanking.events.Users.ValidateUserCommand;
import com.demobanking.events.Users.UserValidatedEvent;
import com.demobanking.repository.AccSagaStateRepository;
import com.demobanking.request.AccountRequest;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountOrchestratorService implements IAccountOrchestratorService{

    private final KafkaTemplate<String, Message> template;
    private final AccSagaStateRepository sagaStateRepository;

    public String initiateAccountCreation(com.demobanking.request.AccountRequest accountRequest){
        String idempotencyKey = accountRequest.getIdempotencyKey();
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = sagaStateRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                // Same request seen before (e.g. a client retry after a timeout) - hand back
                // the saga already in flight instead of starting a duplicate one.
                return existing.get().getSagaId();
            }
        }

        String sagaId = UUID.randomUUID().toString();
        AccountSagaState accountSagaState = new AccountSagaState(
                sagaId,
                null, // account # not yet generated at the beginning of the process
                accountRequest.getAccountType(),
                accountRequest.getUserId(),
                AccountSagaStatus.STARTED,
                AccountSagaStep.OPEN_ACCOUNT,
                idempotencyKey
        );
        sagaStateRepository.save(accountSagaState);
        // Step 1 - Send command to validate user
        validateUser(accountSagaState.getSagaId(), accountRequest);

        return sagaId;
    }

    public AccountSagaState retrieveSagaStateById(String sagaId){
        return sagaStateRepository.findById(sagaId).orElseThrow();
    }

    public void validateUser(String sagaId, AccountRequest accountRequest){
        AccountSagaState accountSagaState = sagaStateRepository.findById(sagaId).orElseThrow();
        accountSagaState.setCurrentStep(AccountSagaStep.VALIDATE_USER);
        accountSagaState.setAccountSagaStatus(AccountSagaStatus.PROCESSING);
        sagaStateRepository.save(accountSagaState);

        ValidateUserCommand validateUserCommand =
                ValidateUserCommand.newBuilder()
                        .setSagaId(sagaId)
                        .setUserId(accountRequest.getUserId())
                        .build();
        template.send("VALIDATE_USER_CMD", validateUserCommand);
    }

    public void createAccount(AccountSagaState accountSagaState){
        // Reuse the account number on a redelivered event instead of minting a fresh one each
        // time - otherwise a duplicate USER_VALIDATED_TOPIC delivery would open two accounts.
        String accountNumber = accountSagaState.getAccountNumber();
        if (accountNumber == null) {
            accountNumber = "OB-" + UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            accountSagaState.setAccountNumber(accountNumber);
        }
        accountSagaState.setCurrentStep(AccountSagaStep.CREATE_ACCOUNT);
        sagaStateRepository.save(accountSagaState);

        CreateAccountCommand createAccountCommand = CreateAccountCommand.newBuilder()
                .setSagaId(accountSagaState.getSagaId())
                .setAccountNumber(accountNumber)
                .setUserId(accountSagaState.getUserId())
                .setAccountType(accountSagaState.getAccountType())
                .build();

        template.send("CREATE_ACCOUNT_CMD", createAccountCommand);
    }

    @KafkaListener(
            topics = "USER_VALIDATED_TOPIC",
            containerFactory = "userEventListenerFactory", groupId = "user-service-group")
    public void onUserValidate(UserValidatedEvent event) {
        AccountSagaState accountSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        // Already moved past this step - a redelivered/duplicate event, ignore it.
        if (accountSagaState.getCurrentStep() != AccountSagaStep.VALIDATE_USER) {
            return;
        }

        if(event.getValidated()){
            createAccount(accountSagaState);
        }
    }

    @KafkaListener(
            topics = "USER_NOT_VALIDATED_TOPIC",
            containerFactory = "userFailEventListenerFactory",
            groupId = "user-service-group")
    public void onUserNotValidate(UserNotValidatedEvent userNotValidatedEvent) {
        AccountSagaState accountSagaState = sagaStateRepository.findById(userNotValidatedEvent.getSagaId()).orElseThrow();
        if (accountSagaState.getAccountSagaStatus() == AccountSagaStatus.FAILED
                || accountSagaState.getAccountSagaStatus() == AccountSagaStatus.COMPLETED) {
            return;
        }
        accountSagaState.setCurrentStep(AccountSagaStep.REJECT_ACCOUNT);
        accountSagaState.setAccountSagaStatus(AccountSagaStatus.FAILED);
        sagaStateRepository.save(accountSagaState);
    }

    @KafkaListener(
            topics = "ACCOUNT_CREATED_EVENTS_TOPIC",
            groupId = "account-orchestrator-group",
            containerFactory = "accountEventListenerFactory")
    public void onAccountCreation(AccountCreatedEvent event) {
        AccountSagaState accountSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        if (accountSagaState.getCurrentStep() != AccountSagaStep.CREATE_ACCOUNT) {
            return;
        }
        accountSagaState.setCurrentStep(AccountSagaStep.CONFIRM_ACCOUNT);
        accountSagaState.setAccountSagaStatus(AccountSagaStatus.COMPLETED);
        sagaStateRepository.save(accountSagaState);
    }

    @KafkaListener(
            topics = "ACCOUNT_NOT_CREATED_EVENTS_TOPIC",
            groupId = "account-orchestrator-group",
            containerFactory = "accountNotCreatedEventListenerFactory")
    public void onAccountNotCreated(AccountNotCreatedEvent event) {
        AccountSagaState accountSagaState = sagaStateRepository.findById(event.getSagaId()).orElseThrow();
        if (accountSagaState.getCurrentStep() != AccountSagaStep.CREATE_ACCOUNT) {
            return;
        }
        accountSagaState.setCurrentStep(AccountSagaStep.REJECT_ACCOUNT);
        accountSagaState.setAccountSagaStatus(AccountSagaStatus.FAILED);
        sagaStateRepository.save(accountSagaState);
        IO.println("ACCOUNT NOT CREATED! : " + event.getMessage());
    }
}
