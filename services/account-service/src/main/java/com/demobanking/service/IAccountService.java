package com.demobanking.service;

import com.demobanking.controller.CreateAccountCommand;
import com.demobanking.dto.AccountDTO;

import java.util.List;


public interface IAccountService {
    void openAccount(CreateAccountCommand createAccountCommand);
    void updateAccount(String accountNumber, AccountDTO accountDTO);
    void enableAccount(String accountNumber, AccountDTO accountDTO);
    void freezeAccount(String accountNumber, AccountDTO accountDTO);
    void disableAccount(String accountNumber, AccountDTO accountDTO);
    void registerLogActivityAcc(Long id); // TODO: for simplicity as an example for now.
    void reverseLogActivityAcc(Long id); // TODO: for simplicity as an example for now.
    AccountDTO retrieveAccountByAccNumber(String accountNumber);
    List<AccountDTO> retrieveAllAccounts();
}
