package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    void createTransaction(CreateTransactionCommand createTransactionCommand);
    void transfer(TransferCommand transferCommand);
    TransactionDTO retrieveTransactionById(UUID id);
    List<TransactionDTO> retrieveAllTransactions();
    void cancelTransaction(UUID transactionId);

}
