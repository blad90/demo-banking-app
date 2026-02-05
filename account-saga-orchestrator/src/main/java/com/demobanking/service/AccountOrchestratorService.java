package com.demobanking.service;

import com.demobanking.events.Accounts.AccountCreatedEvent;
import com.demobanking.events.Users.ValidateUserCommand;
import com.demobanking.events.Users.UserValidatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountOrchestratorService implements IAccountOrchestratorService{

    private final KafkaTemplate<String, Object> template;

    @KafkaListener(
            topics = "USER_VALIDATED_TOPIC",
            groupId = "account-orchestrator-group")
    public void onUserValidate(UserValidatedEvent event){
        IO.println("USER VALIDATED! : " + event);
    }

    @KafkaListener(
            topics = "USER_NOT_VALIDATED_TOPIC",
            groupId = "account-orchestrator-group")
    public void onUserNotValidate(UserValidatedEvent event){
        IO.println("USER NOT VALIDATED! : " + event);
    }

    @KafkaListener(
            topics = "ACCOUNT_CREATED_EVENTS_TOPIC",
            groupId = "account-orchestrator-group")
    public void onAccountCreation(AccountCreatedEvent event){
        IO.println(event);
        IO.println("onAccountCreation!!!");

        ValidateUserCommand validateUserCommand =
                ValidateUserCommand.newBuilder()
                        .setUserId(event.getUserId())
                        .build();

    template.send("VALIDATE_USER_CMD", validateUserCommand);
    }
}
