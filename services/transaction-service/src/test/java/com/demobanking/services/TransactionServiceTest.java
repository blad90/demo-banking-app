package com.demobanking.services;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import com.demobanking.events.Transactions.CancelTransactionCommand;
import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.listener.TransactionEventProducer;
import com.demobanking.repository.ITransactionRepository;
import com.demobanking.service.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock
    private TransactionEventProducer transactionEventProducer;

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

    @Test
    @DisplayName("Test Case: Creating a DEBIT transaction persists it against a single account and publishes it")
    public void testCreateTransactionDebit(){
        String correlationId = UUID.randomUUID().toString();
        CreateTransactionCommand command = CreateTransactionCommand.newBuilder()
                .setSagaId("saga-1")
                .setCorrelationId(correlationId)
                .setAccountNumber("OB-SOURCE1")
                .setCustomerId("1")
                .setTransactionType(TransactionType.DEBIT)
                .setDescription("ATM withdrawal")
                .setAmount("40.00")
                .build();

        transactionService.createTransaction(command);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        Assertions.assertEquals("OB-SOURCE1", saved.getSourceAccountNumber());
        Assertions.assertNull(saved.getDestinationAccountNumber());
        Assertions.assertEquals(1L, saved.getCustomerId());
        Assertions.assertEquals(TransactionType.DEBIT, saved.getType());
        Assertions.assertEquals(new BigDecimal("40.00"), saved.getTransactionAmount());
        verify(transactionEventProducer).publishTransactionCreated("saga-1", saved);
    }

    @Test
    @DisplayName("Test Case: A redelivered CREATE_TRANSACTION_CMD for an existing correlationId is not recorded twice")
    public void testCreateTransactionIsIdempotentOnRedelivery(){
        UUID correlationId = UUID.randomUUID();
        Transaction existingTransaction = new Transaction(
                correlationId, "OB-SOURCE1", 1L, null,
                "ATM withdrawal", new BigDecimal("40.00"), TransactionType.DEBIT);

        when(transactionRepository.findByCorrelationId(correlationId)).thenReturn(Optional.of(existingTransaction));

        CreateTransactionCommand command = CreateTransactionCommand.newBuilder()
                .setSagaId("saga-2")
                .setCorrelationId(correlationId.toString())
                .setAccountNumber("OB-SOURCE1")
                .setCustomerId("1")
                .setTransactionType(TransactionType.DEBIT)
                .setDescription("ATM withdrawal")
                .setAmount("40.00")
                .build();

        transactionService.createTransaction(command);

        verify(transactionRepository, never()).save(Mockito.any(Transaction.class));
        verify(transactionEventProducer).publishTransactionCreated("saga-2", existingTransaction);
    }

    @Test
    @DisplayName("Test Case: Cancelling by correlationId marks the transaction TRAN_CANCELLED and confirms back")
    public void testCancelTransactionByCorrelationId(){
        UUID correlationId = UUID.randomUUID();
        Transaction existingTransaction = new Transaction(
                correlationId, "OB-SOURCE1", 1L, null,
                "ATM withdrawal", new BigDecimal("40.00"), TransactionType.DEBIT);
        existingTransaction.setTransactionState(TransactionState.TRAN_COMPLETED);

        when(transactionRepository.findByCorrelationId(correlationId)).thenReturn(Optional.of(existingTransaction));

        CancelTransactionCommand command = CancelTransactionCommand.newBuilder()
                .setSagaId("saga-3")
                .setCorrelationId(correlationId.toString())
                .setMessage("insufficient funds")
                .build();

        transactionService.cancelTransactionByCorrelationId(command);

        Assertions.assertEquals(TransactionState.TRAN_CANCELLED, existingTransaction.getTransactionState());
        verify(transactionRepository).save(existingTransaction);
        verify(transactionEventProducer).publishTransactionCancelled("saga-3", correlationId);
    }
}
