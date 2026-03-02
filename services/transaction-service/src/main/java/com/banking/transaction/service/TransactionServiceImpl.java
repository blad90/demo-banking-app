package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.listener.TransactionEventProducer;
import com.banking.transaction.repository.ITransactionRepository;
import com.banking.transaction.utils.TransactionMapper;
import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements ITransactionService{

    private final TransactionEventProducer transactionEventProducer;
    private ITransactionRepository transactionRepository;

    @Override
    public void createTransaction(CreateTransactionCommand createTransactionCommand) {
//        Transaction newTransaction = new Transaction(
//                createTransactionCommand.getCorrelationId(),
//                createTransactionCommand.getDescription(),
//                createTransactionCommand.getA
//        )
//        newTransaction.setTransactionState(TransactionState.TRAN_INITIATED);
//        newTransaction.setType(type);

//        transactionRepository.save(newTransaction);
    }

    @Override
    public void transfer(TransferCommand transferCommand) {

        Transaction newTransaction = new Transaction(
                UUID.fromString(transferCommand.getCorrelationId()),
                transferCommand.getSourceAccountNumber(),
                transferCommand.getDestinationAccountNumber(),
                transferCommand.getDescription(),
                new BigDecimal(transferCommand.getAmount()),
                TransactionType.TRANSFER);
        newTransaction.setTransactionState(TransactionState.TRAN_PROCESSING);

        transactionRepository.save(newTransaction);

        transactionEventProducer.publishTransactionCreated(newTransaction);
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
    public Page<TransactionDTO> findAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(TransactionMapper::mapToDTO);
    }

    @Override
    public Page<TransactionDTO> findAllFilteredTransactions(String description, Pageable pageable) {
        return transactionRepository
                .findAllByDescriptionContainingIgnoreCase(description, pageable)
                .map(TransactionMapper::mapToDTO);
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
