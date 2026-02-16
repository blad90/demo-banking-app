package com.banking.transaction.utils;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.entity.Transaction;

import java.util.UUID;

public class TransactionMapper {
    public static Transaction mapToEntity(TransactionDTO transactionDTO){
        Transaction transaction = new Transaction(
                transactionDTO.getId(),
                transactionDTO.getCorrelationId(),
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
