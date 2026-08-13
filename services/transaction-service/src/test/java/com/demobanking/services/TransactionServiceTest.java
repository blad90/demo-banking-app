package com.demobanking.services;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.repository.ITransactionRepository;
import com.demobanking.service.TransactionServiceImpl;
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
import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private ITransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Test Case: Verifying the retrieval of all transactions by page")
    public void testFindAllTransactions(){
        List<Transaction> transactions = List.of(new Transaction(), new Transaction(), new Transaction());
        Page<Transaction> transactionsPage = new PageImpl<>(transactions);

        when(transactionRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(transactionsPage);

        Page<TransactionDTO> retrievedTransactions = transactionService
                .findAllTransactions(Pageable.ofSize(5));
        Assertions.assertEquals(3, retrievedTransactions.getTotalElements());
        verify(transactionRepository).findAll(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Test Case: Retrieving an existing transaction by id returns its mapped DTO")
    public void testRetrieveTransactionByIdFound(){
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(
                UUID.randomUUID(), "OB-SOURCE1", 1L, "OB-DEST001",
                "test transfer", new BigDecimal("30.00"), TransactionType.TRANSFER);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        TransactionDTO result = transactionService.retrieveTransactionById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("OB-SOURCE1", result.getSourceAccount());
        Assertions.assertEquals("OB-DEST001", result.getDestinationAccount());
        Assertions.assertEquals(new BigDecimal("30.00"), result.getTransactionAmount());
        verify(transactionRepository, never()).save(Mockito.any(Transaction.class));
    }

    @Test
    @DisplayName("Test Case: Retrieving a missing transaction by id returns null instead of throwing")
    public void testRetrieveTransactionByIdNotFound(){
        UUID id = UUID.randomUUID();
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        TransactionDTO result = transactionService.retrieveTransactionById(id);

        Assertions.assertNull(result);
    }
}
