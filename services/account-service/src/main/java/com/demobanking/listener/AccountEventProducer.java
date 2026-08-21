package com.demobanking.listener;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;
import com.demobanking.events.Accounts.FailureReason;
import com.demobanking.events.Accounts.AccountNotValidatedEvent;
import com.demobanking.events.Accounts.AccountsBalancesUpdatedEvent;
import com.demobanking.events.Accounts.AccountValidatedEvent;
import com.demobanking.events.Accounts.AccountCreatedEvent;
import com.demobanking.events.Accounts.AccountBalanceUpdatedEvent;
import com.demobanking.events.Accounts.BalanceUpdateFailedEvent;
import com.demobanking.events.Accounts.AccountNotCreatedEvent;
import com.demobanking.events.Accounts.AccountState;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer {

    private final KafkaTemplate<String, byte[]> template;

    public void publishAccountCreated(String sagaId, Account account){
        AccountCreatedEvent accountCreatedEvent = AccountCreatedEvent.newBuilder()
                .setAccountNumber(account.getAccountNumber())
                .setUserId(account.getCustomerId())
                .setAccountState(AccountState.ACCOUNT_CREATED)
                .setSagaId(sagaId)
                .build();
        byte[] data = accountCreatedEvent.toByteArray();
        template.send("ACCOUNT_CREATED_EVENTS_TOPIC", account.getAccountNumber(), data);
    }

    public void publishAccountValidated(String sagaId, AccountDTO accountDTO, AccountDTO destinationAccountDTO){
        AccountValidatedEvent.Builder builder = AccountValidatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber(accountDTO.getAccountNumber())
                .setAccountState(AccountState.ACCOUNT_VALIDATED)
                .setValidated(true);
        if (destinationAccountDTO != null) {
            builder.setDestinationAccountNumber(destinationAccountDTO.getAccountNumber());
        }
        byte[] data = builder.build().toByteArray();
        template.send("ACCOUNT_VALIDATED_EVENTS_TOPIC", accountDTO.getAccountNumber(), data);
    }

    public void publishAccountsBalancesUpdated(String sagaId, Account sourceAccount, Account destinationAccount){
        AccountsBalancesUpdatedEvent accountsBalancesUpdatedEvent = AccountsBalancesUpdatedEvent.newBuilder()
                .setSourceAccountNumber(sourceAccount.getAccountNumber())
                .setDestinationAccountNumber(destinationAccount.getAccountNumber())
                .setSagaId(sagaId)
                .build();
        byte[] data = accountsBalancesUpdatedEvent.toByteArray();
        template.send("ACCOUNTS_BALANCES_UPDATED_EVENTS_TOPIC", sagaId, data);
    }

    public void publishAccountNotValidated(String sagaId, String message){
        AccountNotValidatedEvent accountNotValidatedEvent = AccountNotValidatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setFailureReason(FailureReason.ACCOUNT_NOT_FOUND)
                .setMessage(message)
                .setValidated(false)
                .build();
        byte[] data = accountNotValidatedEvent.toByteArray();
        template.send("ACCOUNT_NOT_VALIDATED_TOPIC", sagaId, data);
    }

    public void publishAccountBalanceUpdated(String sagaId, Account account){
        AccountBalanceUpdatedEvent accountBalanceUpdatedEvent = AccountBalanceUpdatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber(account.getAccountNumber())
                .build();
        byte[] data = accountBalanceUpdatedEvent.toByteArray();
        template.send("ACCOUNT_BALANCE_UPDATED_EVENTS_TOPIC", sagaId, data);
    }

    public void publishBalanceUpdateFailed(String sagaId, String accountNumber, String message){
        BalanceUpdateFailedEvent balanceUpdateFailedEvent = BalanceUpdateFailedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber(accountNumber)
                .setMessage(message)
                .build();
        byte[] data = balanceUpdateFailedEvent.toByteArray();
        template.send("BALANCE_UPDATE_FAILED_EVENTS_TOPIC", sagaId, data);
    }

    public void publishAccountNotCreated(String sagaId, String accountNumber, String message){
        AccountNotCreatedEvent accountNotCreatedEvent = AccountNotCreatedEvent.newBuilder()
                .setSagaId(sagaId)
                .setAccountNumber(accountNumber)
                .setMessage(message)
                .build();
        byte[] data = accountNotCreatedEvent.toByteArray();
        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", sagaId, data);
    }
}
