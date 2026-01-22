package com.banking.transaction.utils;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.entity.Transaction;

public class TransactionMapper {
    public static Transaction mapToEntity(TransactionDTO transactionDTO){
        Transaction transaction = new Transaction(
                transactionDTO.getId(),
                transactionDTO.getDescription(),
                transactionDTO.getTransactionAmount(),
                transactionDTO.getTransactionDate(),
                transactionDTO.getType(),
                transactionDTO.getTransactionState()
        );
        return transaction;
    }

    public static TransactionDTO mapToDTO(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getType(),
                transaction.getTransactionState()
        );
        return transactionDTO;
    }
}
