package com.banking.account.service;

import com.banking.account.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAccountService {
    void openAccount();
    void updateAccount();
    void disableAccount();
    Account retrieveAccountByAccNumber();
    List<Account> retrieveAllAccounts();
}
