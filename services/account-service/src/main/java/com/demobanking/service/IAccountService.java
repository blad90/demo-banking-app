package com.demobanking.service;

import com.demobanking.dto.AccountDTO;
import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.CreateAccountCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IAccountService {
    void openAccount(CreateAccountCommand createAccountCommand);
    void updateAccountsBalances(UpdateAccountsBalancesCommand updateAccountsBalancesCommand);
    void updateAccount(String accountNumber, AccountDTO accountDTO);
    void enableAccount(String accountNumber, AccountDTO accountDTO);
    void freezeAccount(String accountNumber, AccountDTO accountDTO);
    void disableAccount(String accountNumber, AccountDTO accountDTO);
    void registerLogActivityAcc(Long id); // TODO: for simplicity as an example for now.
    void reverseLogActivityAcc(Long id); // TODO: for simplicity as an example for now.
    AccountDTO retrieveAccountByAccNumber(String accountNumber);
    List<AccountDTO> retrieveAllAccounts();
    Page<AccountDTO> findAllAccounts(Pageable pageable);
    Page<AccountDTO> findAllFilteredAccounts(String query, Pageable pageable);
    List<AccountDTO> retrieveAllAccountsByCustomerId(String customerId);
}
