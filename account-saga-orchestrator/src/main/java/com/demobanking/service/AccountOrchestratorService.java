package com.demobanking.service;

import com.demobanking.events.AccountCreatedEvent;
import com.demobanking.events.UserValidatedEvent;
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
            topics = "ACCOUNT_CREATED_EVENTS_TOPIC",
            groupId = "account-orchestrator-group")
    public void onAccountCreation(AccountCreatedEvent event){
        IO.println(event);
        IO.println("onAccountCreation!!!");
        template.send("VALIDATE_USER_CMD", event.userId());
    }
}
