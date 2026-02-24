package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    void createTransaction(CreateTransactionCommand createTransactionCommand);
    void transfer(TransferCommand transferCommand);
    TransactionDTO retrieveTransactionById(UUID id);
    List<TransactionDTO> retrieveAllTransactions();
    Page<TransactionDTO> findAllTransactions(Pageable pageable);
    Page<TransactionDTO> findAllFilteredTransactions(String description, Pageable pageable);
    void cancelTransaction(UUID transactionId);

}
