package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.entity.TransactionType;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    void createTransaction(TransactionDTO transactionDTO, TransactionType type);
    TransactionDTO retrieveTransactionById(UUID id);
    List<TransactionDTO> retrieveAllTransactions();
    void cancelTransaction(UUID transactionId);

}
