package com.banking.account.service;

import com.banking.account.dto.AccountDTO;
import com.banking.account.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IAccountService {
    void openAccount(Long userId, AccountDTO accountDTO);
    Boolean updateAccount(String accountNumber, AccountDTO accountDTO);
    Boolean disableAccount(String accountNumber, AccountDTO accountDTO);
    AccountDTO retrieveAccountByAccNumber(String accountNumber);
    List<AccountDTO> retrieveAllAccounts();
}
