package com.demobanking.listener;

import com.demobanking.entity.Account;
import com.demobanking.events.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer {

    private final KafkaTemplate<String, Object> template;

    public void publishAccountCreated(Account account){
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(
                account.getAccountNumber(),
                account.getCustomer(),
                account.getAccountState()
        );
        template.send("ACCOUNT_CREATED_EVENTS_TOPIC", account.getAccountNumber(), accountCreatedEvent);
    }
}
