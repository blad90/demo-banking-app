package com.demobanking.service;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;
import com.demobanking.entity.AccountState;
import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.ValidateAccountCommand;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.exceptions.BankAccountNotFoundException;
import com.demobanking.listener.AccountEventProducer;
import com.demobanking.repository.IAccountRepository;
import com.demobanking.utils.AccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService{

    private final IAccountRepository accountRepository;
    private final AccountEventProducer accountEventProducer;

    @KafkaListener(
            topics = "VALIDATE_ACCOUNT_CMD",
            groupId = "ACCOUNT_EVENT_GROUP",
            containerFactory = "validateAcctKafkaListenerContainerFactory")
    public void onAccountValidate(ValidateAccountCommand validateAccountCommand)  {

        try{
            Account account = accountRepository
                    .findAccountByAccountNumber(validateAccountCommand.getAccountNumber())
                    .orElseThrow(() -> new BankAccountNotFoundException(validateAccountCommand.getAccountNumber()));
            AccountDTO accountDTO = AccountMapper.mapToDTO(account.getCustomer(), account);
            accountEventProducer.publishAccountValidated(validateAccountCommand.getSagaId(), accountDTO);
        } catch (BankAccountNotFoundException e){
            accountEventProducer.publishAccountNotValidated(validateAccountCommand.getSagaId(), e.getMessage());
        }
    }

    public void openAccount(CreateAccountCommand createAccountCommand) {

        Account newAccount = new Account(
                createAccountCommand.getAccountNumber(),
                createAccountCommand.getUserId(),
                createAccountCommand.getAccountType(),
                AccountState.ACCOUNT_CREATED);
        newAccount.setBalance(BigDecimal.valueOf(0.00));
        accountRepository.save(newAccount);
        accountEventProducer.publishAccountCreated(newAccount);
    }

    @Override
    public void updateAccountsBalances(UpdateAccountsBalancesCommand updateAccountsBalancesCommand) {
        Account sourceAccount = accountRepository.findAccountByAccountNumber(updateAccountsBalancesCommand.getSourceAccountNumber()).get();
        Account destinationAccount = accountRepository.findAccountByAccountNumber(updateAccountsBalancesCommand.getDestinationAccountNumber()).get();
        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal destinationBalance = destinationAccount.getBalance();

        sourceAccount.setBalance(sourceBalance.subtract((new BigDecimal(updateAccountsBalancesCommand.getAmount()))));

        destinationAccount.setBalance(destinationBalance.add((new BigDecimal(updateAccountsBalancesCommand.getAmount()))));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        accountEventProducer.publishAccountsBalancesUpdated(
                updateAccountsBalancesCommand.getSagaId(),
                sourceAccount,
                destinationAccount);
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
    public Page<AccountDTO> findAllAccounts(Pageable pageable) {
        return accountRepository
                .findAll(pageable)
                .map(account -> AccountMapper.mapToDTO(0L, account));
    }

    @Override
    public Page<AccountDTO> findAllFilteredAccounts(String query, Pageable pageable) {
        return accountRepository
                .findAllByAccountNumberContainingIgnoreCase(query, pageable)
                .map(account -> AccountMapper.mapToDTO(0L, account));
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
