package com.demobanking.listener;

import com.demobanking.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreationConsumer {

    private final IAccountService accountService;

//    @KafkaListener(topics = "ACCOUNT_CREATED_CMD_TOPIC", groupId = "ACCOUNT_EVENT_GROUP")
//    public void onAccountCreateEvent(AccountCreatedEvent event){
//        AccountRequest accountRequest = new AccountRequest(event.accountNumber(), 1L);
//        accountService.openAccount(event.userId(), accountRequest);
//
//        //Successful action
//        accountService.registerLogActivityAcc(event.userId());
//
//        // If opposite, pending to provide rollback logic
//        accountService.reverseLogActivityAcc(event.userId());
//    }
}
