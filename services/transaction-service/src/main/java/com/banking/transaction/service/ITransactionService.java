package com.banking.transaction.service;

import com.banking.transaction.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITransactionService {
    Transaction createTransaction();
    Transaction retrieveTransactionById(Long id);
    List<Transaction> retrieveAllTransactions();
}
