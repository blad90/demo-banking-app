package com.banking.account.service;

import com.banking.account.dto.AccountDTO;
import com.banking.account.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IAccountService {
    void openAccount(Long userId, AccountDTO accountDTO);
    void updateAccount(String accountNumber, AccountDTO accountDTO);
    void enableAccount(String accountNumber, AccountDTO accountDTO);
    void freezeAccount(String accountNumber, AccountDTO accountDTO);
    void disableAccount(String accountNumber, AccountDTO accountDTO);
    AccountDTO retrieveAccountByAccNumber(String accountNumber);
    List<AccountDTO> retrieveAllAccounts();
}
