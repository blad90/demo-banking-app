package com.demobanking.services;

import com.demobanking.dto.AccountDTO;
import com.demobanking.entity.Account;
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

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private IAccountRepository accountRepository;

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
}
