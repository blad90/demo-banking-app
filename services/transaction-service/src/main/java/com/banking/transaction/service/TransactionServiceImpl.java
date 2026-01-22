package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionState;
import com.banking.transaction.entity.TransactionType;
import com.banking.transaction.repository.ITransactionRepository;
import com.banking.transaction.utils.TransactionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements ITransactionService{

    private ITransactionRepository transactionRepository;

    @Override
    public void createTransaction(TransactionDTO transactionDTO, TransactionType type) {
        Transaction newTransaction = TransactionMapper.mapToEntity(transactionDTO);
        newTransaction.setTransactionState(TransactionState.TRAN_INITIATED);
        newTransaction.setType(type);

        transactionRepository.save(newTransaction);
    }

    @Override
    public TransactionDTO retrieveTransactionById(UUID id) {
        Transaction existingTransaction = transactionRepository.findById(id).orElse(null);

        if(existingTransaction != null) transactionRepository.save(existingTransaction);
        return TransactionMapper.mapToDTO(existingTransaction);
    }

    @Override
    public List<TransactionDTO> retrieveAllTransactions() {
        return transactionRepository.findAll()
                .stream().map(TransactionMapper::mapToDTO)
                .toList();
    }

    @Override
    public void cancelTransaction(UUID transactionId) {
        Transaction existingTransaction = transactionRepository.findById(transactionId).orElse(null);

        if(existingTransaction != null) {
            existingTransaction.setTransactionState(TransactionState.TRAN_CANCELLED);
            transactionRepository.save(existingTransaction);
        }
    }
}
