package com.demobanking.service;

import com.demobanking.entity.AccountSagaState;
import com.demobanking.entity.AccountSagaStatus;
import com.demobanking.entity.AccountSagaStep;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.events.Accounts.AccountCreatedEvent;
import com.demobanking.events.Users.ValidateUserCommand;
import com.demobanking.events.Users.UserValidatedEvent;
import com.demobanking.repository.AccSagaStateRepository;
import com.demobanking.request.AccountRequest;
import com.google.protobuf.InvalidProtocolBufferException;
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

    public AccountSagaState initiateAccountCreation(com.demobanking.request.AccountRequest accountRequest){
        AccountSagaState accountSagaState = new AccountSagaState(
                UUID.randomUUID().toString(),
                null, // account # not yet generated at the beginning of the process
                accountRequest.getAccountType(),
                accountRequest.getUserId(),
                AccountSagaStatus.STARTED,
                AccountSagaStep.OPEN_ACCOUNT
        );
        sagaStateRepository.save(accountSagaState);
        // Step 1 - Send command to validate user
        validateUser(accountSagaState.getSagaId(), accountRequest);

        return accountSagaState;
    }

    public void validateUser(String sagaId, AccountRequest accountRequest){
        ValidateUserCommand validateUserCommand =
                ValidateUserCommand.newBuilder()
                        .setSagaId(sagaId)
                        .setUserId(accountRequest.getUserId())
                        .build();
        template.send("VALIDATE_USER_CMD", validateUserCommand);
    }

    public void createAccount(AccountSagaState accountSagaState){
        CreateAccountCommand createAccountCommand = CreateAccountCommand.newBuilder()
                .setSagaId(accountSagaState.getSagaId())
                .setAccountNumber("OB-" + UUID.randomUUID().toString().substring(0,7).toUpperCase())
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
        accountSagaState.setCurrentStep(AccountSagaStep.VALIDATE_USER);
        sagaStateRepository.save(accountSagaState);

        if(event.getValidated()){
            accountSagaState.setCurrentStep(AccountSagaStep.CONFIRM_ACCOUNT);
            sagaStateRepository.save(accountSagaState);
            createAccount(accountSagaState);

        } else {
            // compensation logic
            IO.println("Compensating... [PENDING LOGIC]");
            accountSagaState.setCurrentStep(AccountSagaStep.REJECT_ACCOUNT);
            sagaStateRepository.save(accountSagaState);
            UserValidatedEvent userValidatedEvent = UserValidatedEvent.newBuilder()
                    .setSagaId(accountSagaState.getSagaId())
                    .setUserId(0L)
                    .setValidated(false)
                    .build();
            template.send("USER_NOT_VALIDATED_TOPIC", userValidatedEvent);
        }
    }

    @KafkaListener(
            topics = "USER_NOT_VALIDATED_TOPIC",
            groupId = "account-orchestrator-group")
    public void onUserNotValidate(UserValidatedEvent userValidatedEvent) {
        IO.println("USER NOT VALIDATED! : " + userValidatedEvent);
    }

    @KafkaListener(
            topics = "ACCOUNT_CREATED_EVENTS_TOPIC",
            groupId = "account-orchestrator-group",
            containerFactory = "accountEventListenerFactory")
    public void onAccountCreation(AccountCreatedEvent event) {
        IO.println(event);
        IO.println("onAccountCreation!!!");
    }
}
