package com.demobanking.service;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    void createTransaction(CreateTransactionCommand createTransactionCommand);
    void transfer(String sagaId, TransferCommand transferCommand);
    TransactionDTO retrieveTransactionById(UUID id);
    List<TransactionDTO> retrieveAllTransactions();
    Page<TransactionDTO> findAllTransactions(Pageable pageable);
    Page<TransactionDTO> findAllFilteredTransactions(String description, Pageable pageable);
    Page<TransactionDTO> findAllByCustomerId(Long customerId, Pageable pageable);
    void cancelTransaction(UUID transactionId);

}
