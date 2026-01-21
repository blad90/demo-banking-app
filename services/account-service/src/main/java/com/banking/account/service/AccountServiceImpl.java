package com.banking.account.service;

import com.banking.account.dto.AccountDTO;
import com.banking.account.entity.Account;
import com.banking.account.entity.AccountState;
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
    public Boolean updateAccount(String accountNumber, AccountDTO accountDTO) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElse(null);
        Account accountToUpdate;

        if(account != null){
            accountToUpdate = AccountMapper.mapToEntityUpdate(accountDTO, account);
            accountRepository.save(accountToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public Boolean disableAccount(String accountNumber, AccountDTO accountDTO) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElse(null);
        Account accountToDisable;

        if(account != null){
            accountToDisable = AccountMapper.mapToEntityUpdate(accountDTO, account);
            accountToDisable.setAccountState(AccountState.ACCOUNT_INACTIVE);
            accountRepository.save(accountToDisable);
            return true;
        }
        return false;
    }

    @Override
    public AccountDTO retrieveAccountByAccNumber(String accountNumber) {
        Account retrievedAccount = accountRepository.findAccountByAccountNumber(accountNumber).get();
        // TODO: Retrieve user correctly according to ID.
        return AccountMapper.mapToDTO(0L, retrievedAccount);
    }

    @Override
    public List<AccountDTO> retrieveAllAccounts() {
        return accountRepository.findAll()
                .stream()
                // TODO: Retrieve user correctly according to ID.
                .map(account -> AccountMapper.mapToDTO(0L, account))
                .toList();
    }
}
