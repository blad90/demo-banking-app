package com.demobanking.service;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;
import com.demobanking.entity.AccountState;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.exceptions.BankAccountNotFoundException;
import com.demobanking.listener.AccountEventProducer;
import com.demobanking.repository.IAccountRepository;
import com.demobanking.utils.AccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService{

    private final IAccountRepository accountRepository;
    private final AccountEventProducer accountEventProducer;

    public void openAccount(CreateAccountCommand createAccountCommand) {

        Account newAccount = new Account(
                createAccountCommand.getAccountNumber(),
                createAccountCommand.getUserId(),
                createAccountCommand.getAccountType(),
                AccountState.ACCOUNT_CREATED);
        newAccount.setBalance(0.00);
        accountRepository.save(newAccount);
        accountEventProducer.publishAccountCreated(newAccount);
    }

    @Override
    public void updateAccount(String accountNumber, AccountDTO accountDTO) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()-> new BankAccountNotFoundException(accountNumber));

        Account accountToUpdate = AccountMapper.mapToEntityUpdate(accountDTO, account);
        accountRepository.save(accountToUpdate);
    }



    private void changeAccountState(String accountNumber, AccountDTO accountDTO, AccountState accountState) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()-> new BankAccountNotFoundException(accountNumber));

        Account accountToEnable = AccountMapper.mapToEntityUpdate(accountDTO, account);
        accountToEnable.setAccountState(accountState);
        accountRepository.save(accountToEnable);
    }

    @Override
    public void enableAccount(String accountNumber, AccountDTO accountDTO) {
        changeAccountState(accountNumber, accountDTO, AccountState.ACCOUNT_ACTIVE);
    }

    @Override
    public void freezeAccount(String accountNumber, AccountDTO accountDTO) {
        changeAccountState(accountNumber, accountDTO, AccountState.ACCOUNT_FROZEN);
    }

    @Override
    public void disableAccount(String accountNumber, AccountDTO accountDTO) {
        changeAccountState(accountNumber, accountDTO, AccountState.ACCOUNT_INACTIVE);
    }

    @Override
    public void registerLogActivityAcc(Long id) {
        IO.println("### SIMULATING REGISTERING A LOG ACTIVITY FOR ACCOUNT CREATION ###");
    }

    @Override
    public void reverseLogActivityAcc(Long id) {
        IO.println("### SIMULATING REVERSING A LOG ACTIVITY FOR ACCOUNT CREATION ###");
    }

    @Override
    public AccountDTO retrieveAccountByAccNumber(String accountNumber) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()-> new BankAccountNotFoundException(accountNumber));
        return AccountMapper.mapToDTO(0L, account);
    }

    @Override
    public List<AccountDTO> retrieveAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> AccountMapper.mapToDTO(0L, account))
                .toList();
    }

    @Override
    public List<AccountDTO> retrieveAllAccountsByCustomerId(String customerId) {
        return accountRepository.findAccountsByCustomer(customerId)
                .stream()
                .flatMap(Collection::stream)
                .map(account -> AccountMapper.mapToDTO(0L, account))
                .toList();
    }
}
