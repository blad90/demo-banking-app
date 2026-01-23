package com.banking.account.service;

import com.banking.account.dto.AccountDTO;
import com.banking.account.entity.Account;
import com.banking.account.entity.AccountState;
import com.banking.account.exceptions.BankAccountNotFoundException;
import com.banking.account.repository.IAccountRepository;
import com.banking.account.utils.AccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService{

    private IAccountRepository accountRepository;

    @Override
    public void openAccount(Long userId, AccountDTO accountDTO) {
        // TODO: Retrieve user correctly according to ID.
        Account newAccount = AccountMapper.mapToEntity(userId, accountDTO);
        newAccount.setAccountState(AccountState.ACCOUNT_CREATED);

        accountRepository.save(newAccount);
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
}
