package com.demobanking.utils;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;

public class AccountMapper {
    public static AccountDTO mapToDTO(Long userId, Account account){
        AccountDTO accountDTO = new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getCustomer(), // TODO: Retrieve user correctly according to ID.
                account.getBalance(),
                account.getAccountType(),
                account.getAccountCreationDate(),
                account.getAccountLastUpdated(),
                account.getAccountState()
        );
        return accountDTO;
    }

    public static Account mapToEntity(Long userId, AccountDTO accountDTO){
        Account account = new Account(
                accountDTO.getId(),
                accountDTO.getAccountNumber(),
                accountDTO.getCustomer(), // TODO: Retrieve user correctly according to ID.
                accountDTO.getBalance(),
                accountDTO.getAccountType(),
                accountDTO.getAccountCreationDate(),
                accountDTO.getAccountLastUpdated(),
                accountDTO.getAccountState()
        );
        return account;
    }

    public static Account mapToEntityUpdate(AccountDTO accountDTO, Account account) {
        if(accountDTO.getCustomer() != null) account.setCustomer(accountDTO.getCustomer());
        if(accountDTO.getAccountType() != null) account.setAccountType(accountDTO.getAccountType());
        if(accountDTO.getBalance() != null) account.setBalance(accountDTO.getBalance());
        return account;
    }
}
