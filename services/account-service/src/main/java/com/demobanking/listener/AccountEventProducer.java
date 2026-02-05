package com.demobanking.listener;

import com.demobanking.entity.Account;
import com.demobanking.events.Accounts;
import com.demobanking.events.Accounts.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer {

    private final KafkaTemplate<String, Object> template;

    public void publishAccountCreated(Account account){
        AccountCreatedEvent accountCreatedEvent = AccountCreatedEvent.newBuilder()
                .setAccountNumber(account.getAccountNumber())
                .setUserId(account.getCustomer())
                .setAccountState(Accounts.AccountState.ACCOUNT_CREATED)
                .build();
        template.send("ACCOUNT_CREATED_EVENTS_TOPIC", account.getAccountNumber(), accountCreatedEvent);
    }

    public void publishAccountNotCreated(){
        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", "ERROR TRYING CREATING ACCOUNT", "Account cannot be created");
    }
}
