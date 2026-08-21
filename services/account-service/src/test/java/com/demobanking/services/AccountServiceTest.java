package com.demobanking.services;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;
import com.demobanking.entity.AccountState;
import com.demobanking.events.Accounts.UpdateAccountsBalancesCommand;
import com.demobanking.events.Accounts.UpdateAccountBalanceCommand;
import com.demobanking.events.Accounts.CreateAccountCommand;
import com.demobanking.events.Accounts.BalanceOperation;
import com.demobanking.listener.AccountEventProducer;
import com.demobanking.repository.IAccountRepository;
import com.demobanking.service.AccountServiceImpl;
import com.demobanking.utils.AccountMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private AccountEventProducer accountEventProducer;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test Case: Verifying the retrieval of all accounts by page")
    public void testFindAllAccounts(){
        List<Account> accounts = List.of(new Account(), new Account());
        Page<Account> accountsPage = new PageImpl<>(accounts);

        when(accountRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(accountsPage);

        Page<AccountDTO> retrievedAccounts = accountService
                .findAllAccounts(Pageable.ofSize(5));
        Assertions.assertEquals(2, retrievedAccounts.getTotalElements());
        verify(accountRepository).findAll(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Test Case: Transferring funds debits the source and credits the destination")
    public void testUpdateAccountsBalancesTransfersFunds(){
        Account source = new Account("OB-SOURCE1", 1L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        source.setBalance(new BigDecimal("100.00"));
        Account destination = new Account("OB-DEST001", 2L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        destination.setBalance(new BigDecimal("20.00"));

        when(accountRepository.findAccountByAccountNumber("OB-SOURCE1")).thenReturn(Optional.of(source));
        when(accountRepository.findAccountByAccountNumber("OB-DEST001")).thenReturn(Optional.of(destination));

        UpdateAccountsBalancesCommand command = UpdateAccountsBalancesCommand.newBuilder()
                .setSagaId("saga-1")
                .setSourceAccountNumber("OB-SOURCE1")
                .setDestinationAccountNumber("OB-DEST001")
                .setAmount("30.00")
                .build();

        accountService.updateAccountsBalances(command);

        Assertions.assertEquals(new BigDecimal("70.00"), source.getBalance());
        Assertions.assertEquals(new BigDecimal("50.00"), destination.getBalance());
        verify(accountEventProducer).publishAccountsBalancesUpdated("saga-1", source, destination);
    }

    @Test
    @DisplayName("Test Case: Transferring more than the source balance is rejected without moving any funds")
    public void testUpdateAccountsBalancesRejectsInsufficientFunds(){
        Account source = new Account("OB-SOURCE1", 1L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        source.setBalance(new BigDecimal("10.00"));
        Account destination = new Account("OB-DEST001", 2L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        destination.setBalance(new BigDecimal("20.00"));

        when(accountRepository.findAccountByAccountNumber("OB-SOURCE1")).thenReturn(Optional.of(source));
        when(accountRepository.findAccountByAccountNumber("OB-DEST001")).thenReturn(Optional.of(destination));

        UpdateAccountsBalancesCommand command = UpdateAccountsBalancesCommand.newBuilder()
                .setSagaId("saga-2")
                .setSourceAccountNumber("OB-SOURCE1")
                .setDestinationAccountNumber("OB-DEST001")
                .setAmount("30.00")
                .build();

        accountService.updateAccountsBalances(command);

        Assertions.assertEquals(new BigDecimal("10.00"), source.getBalance());
        Assertions.assertEquals(new BigDecimal("20.00"), destination.getBalance());
        verify(accountRepository, never()).save(Mockito.any(Account.class));
        verify(accountEventProducer).publishBalanceUpdateFailed(
                Mockito.eq("saga-2"), Mockito.eq("OB-SOURCE1"), Mockito.anyString());
        verify(accountEventProducer, never()).publishAccountsBalancesUpdated(
                Mockito.anyString(), Mockito.any(Account.class), Mockito.any(Account.class));
    }

    @Test
    @DisplayName("Test Case: A CREDIT increases a single account's balance")
    public void testUpdateAccountBalanceCredit(){
        Account account = new Account("OB-SOURCE1", 1L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        account.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findAccountByAccountNumber("OB-SOURCE1")).thenReturn(Optional.of(account));

        UpdateAccountBalanceCommand command = UpdateAccountBalanceCommand.newBuilder()
                .setSagaId("saga-3")
                .setAccountNumber("OB-SOURCE1")
                .setOperation(BalanceOperation.CREDIT)
                .setAmount("25.00")
                .build();

        accountService.updateAccountBalance(command);

        Assertions.assertEquals(new BigDecimal("75.00"), account.getBalance());
        verify(accountRepository).save(account);
        verify(accountEventProducer).publishAccountBalanceUpdated("saga-3", account);
    }

    @Test
    @DisplayName("Test Case: A DEBIT decreases a single account's balance")
    public void testUpdateAccountBalanceDebit(){
        Account account = new Account("OB-SOURCE1", 1L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        account.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findAccountByAccountNumber("OB-SOURCE1")).thenReturn(Optional.of(account));

        UpdateAccountBalanceCommand command = UpdateAccountBalanceCommand.newBuilder()
                .setSagaId("saga-4")
                .setAccountNumber("OB-SOURCE1")
                .setOperation(BalanceOperation.DEBIT)
                .setAmount("25.00")
                .build();

        accountService.updateAccountBalance(command);

        Assertions.assertEquals(new BigDecimal("25.00"), account.getBalance());
        verify(accountRepository).save(account);
        verify(accountEventProducer).publishAccountBalanceUpdated("saga-4", account);
    }

    @Test
    @DisplayName("Test Case: A DEBIT larger than the balance is rejected without moving any funds")
    public void testUpdateAccountBalanceDebitRejectsInsufficientFunds(){
        Account account = new Account("OB-SOURCE1", 1L, "CHECKING", AccountState.ACCOUNT_ACTIVE);
        account.setBalance(new BigDecimal("10.00"));

        when(accountRepository.findAccountByAccountNumber("OB-SOURCE1")).thenReturn(Optional.of(account));

        UpdateAccountBalanceCommand command = UpdateAccountBalanceCommand.newBuilder()
                .setSagaId("saga-5")
                .setAccountNumber("OB-SOURCE1")
                .setOperation(BalanceOperation.DEBIT)
                .setAmount("25.00")
                .build();

        accountService.updateAccountBalance(command);

        Assertions.assertEquals(new BigDecimal("10.00"), account.getBalance());
        verify(accountRepository, never()).save(Mockito.any(Account.class));
        verify(accountEventProducer).publishBalanceUpdateFailed(
                Mockito.eq("saga-5"), Mockito.eq("OB-SOURCE1"), Mockito.anyString());
        verify(accountEventProducer, never()).publishAccountBalanceUpdated(Mockito.anyString(), Mockito.any(Account.class));
    }

    @Test
    @DisplayName("Test Case: A redelivered CREATE_ACCOUNT_CMD for an existing account number is not applied twice")
    public void testOpenAccountIsIdempotentOnRedelivery(){
        Account existingAccount = new Account("OB-EXIST01", 1L, "CHECKING", AccountState.ACCOUNT_CREATED);
        existingAccount.setBalance(BigDecimal.ZERO);

        when(accountRepository.findAccountByAccountNumber("OB-EXIST01")).thenReturn(Optional.of(existingAccount));

        CreateAccountCommand command = CreateAccountCommand.newBuilder()
                .setSagaId("saga-6")
                .setAccountNumber("OB-EXIST01")
                .setUserId(1L)
                .setAccountType("CHECKING")
                .build();

        accountService.openAccount(command);

        verify(accountRepository, never()).save(Mockito.any(Account.class));
        verify(accountEventProducer).publishAccountCreated("saga-6", existingAccount);
    }
}
