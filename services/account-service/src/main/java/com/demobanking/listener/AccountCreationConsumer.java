package com.demobanking.listener;

import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreationConsumer {

    private final IAccountService accountService;

    @KafkaListener(topics = "CREATE_ACCOUNT_CMD",
            groupId = "ACCOUNT_EVENT_GROUP",
            containerFactory = "createAcctKafkaListenerContainerFactory")
    public void onAccountCreateEvent(CreateAccountCommand createAccountCommand) {
        accountService.openAccount(createAccountCommand);
    }

    @KafkaListener(topics = "UPDATE_ACCOUNTS_BALANCES_CMD",
            groupId = "ACCOUNT_EVENT_GROUP",
            containerFactory = "updateAcctsBalanceKafkaListenerContainerFactory")
    public void onUpdateAccountsBalancesEvent(UpdateAccountsBalancesCommand updateAccountsBalancesCommand) {
        accountService.updateAccountsBalances(updateAccountsBalancesCommand);
    }
}
