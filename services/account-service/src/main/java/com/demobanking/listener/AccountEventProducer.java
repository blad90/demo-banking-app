package com.demobanking.listener;

import com.demobanking.entity.Account;
import com.demobanking.events.Accounts.AccountsBalancesUpdatedEvent;
import com.demobanking.events.Accounts.AccountValidatedEvent;
import com.demobanking.events.Accounts.AccountCreatedEvent;
import com.demobanking.events.Accounts.AccountState;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer {

    private final KafkaTemplate<String, Message> template;

    public void publishAccountCreated(Account account){
        AccountCreatedEvent accountCreatedEvent = AccountCreatedEvent.newBuilder()
                .setAccountNumber(account.getAccountNumber())
                .setUserId(account.getCustomer())
                .setAccountState(AccountState.ACCOUNT_CREATED)
                .build();
        template.send("ACCOUNT_CREATED_EVENTS_TOPIC", account.getAccountNumber(), accountCreatedEvent);
    }

    public void publishAccountValidated(String sagaId, Account account){
        AccountValidatedEvent accountCreatedEvent = AccountValidatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber(account.getAccountNumber())
                .setAccountState(AccountState.ACCOUNT_VALIDATED)
                .setValidated(true)
                .build();
        template.send("ACCOUNT_VALIDATED_EVENTS_TOPIC", account.getAccountNumber(), accountCreatedEvent);
    }

    public void publishAccountsBalancesUpdated(String sagaId, Account sourceAccount, Account destinationAccount){
        AccountsBalancesUpdatedEvent accountsBalancesUpdatedEvent = AccountsBalancesUpdatedEvent.newBuilder()
                .setSourceAccountNumber(sourceAccount.getAccountNumber())
                .setDestinationAccountNumber(destinationAccount.getAccountNumber())
                .build();
        template.send("ACCOUNTS_BALANCES_UPDATED_EVENTS_TOPIC", sagaId, accountsBalancesUpdatedEvent);
    }

    public void publishAccountNotValidated(String sagaId){
        AccountValidatedEvent accountCreatedEvent = AccountValidatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber("ACC. NOT ABLE TO BE VALIDATED")
                .setAccountState(AccountState.ACCOUNT_NOT_VALIDATED)
                .setValidated(false)
                .build();
        template.send("ACCOUNT_VALIDATED_EVENTS_TOPIC", sagaId, accountCreatedEvent);
    }

//    public void publishAccountNotCreated(){
//        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", "ERROR TRYING CREATING ACCOUNT", "Account cannot be created");
//    }
}
