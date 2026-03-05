package com.demobanking.services;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;
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

import java.util.List;

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
}
