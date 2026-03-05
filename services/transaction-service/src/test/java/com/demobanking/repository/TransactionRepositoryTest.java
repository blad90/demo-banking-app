package com.demobanking.repository;


import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions.TransactionType;
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
public class TransactionRepositoryTest {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Test
    @DisplayName("Test Case: Verifying if transactions can be created then be retrieved")
    public void testTransactionRepoSaveAllAndReturnSavedTransaction(){
        Transaction transaction = Transaction.builder()
                .transactionAmount(new BigDecimal("875.75"))
                .description("Test Transaction Description")
                .type(TransactionType.TRANSFER)
                .sourceAccountNumber("OB-XXXXXX")
                .destinationAccountNumber("OB-YYYYY")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        Assertions.assertNotNull(savedTransaction);
    }
}
