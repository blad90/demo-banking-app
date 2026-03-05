package com.demobanking.repository;


import com.demobanking.entity.Account;
import com.demobanking.entity.AccountState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {

    @Autowired
    private IAccountRepository accountRepository;

    @Test
    @DisplayName("Test Case: Verifying if account can be created then be retrieved")
    public void testAccountRepoSaveAllAndReturnSavedAccount(){
        Account testAccount = Account.builder()
                .accountType("SAVINGS")
                .balance(new BigDecimal("2500.70"))
                .accountState(AccountState.ACCOUNT_CREATED)
                .build();

        Account savedAccount = accountRepository.save(testAccount);

        Assertions.assertNotNull(savedAccount);
    }
}
