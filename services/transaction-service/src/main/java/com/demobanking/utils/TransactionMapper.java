package com.demobanking.utils;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;

public class TransactionMapper {
    public static Transaction mapToEntity(TransactionDTO transactionDTO){
        Transaction transaction = new Transaction(
                transactionDTO.getId(),
                transactionDTO.getCorrelationId(),
                transactionDTO.getCustomerId(),
                transactionDTO.getSourceAccount(),
                transactionDTO.getDestinationAccount(),
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
                transaction.getCorrelationId(),
                transaction.getCustomerId(),
                transaction.getSourceAccountNumber(),
                transaction.getDestinationAccountNumber(),
                transaction.getDescription(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getType(),
                transaction.getTransactionState()
        );
        return transactionDTO;
    }
}
