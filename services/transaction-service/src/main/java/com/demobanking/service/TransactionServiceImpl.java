package com.demobanking.service;

import com.demobanking.dto.TransactionDTO;
import com.demobanking.entity.Transaction;
import com.demobanking.listener.TransactionEventProducer;
import com.demobanking.repository.ITransactionRepository;
import com.demobanking.utils.TransactionMapper;
import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import com.demobanking.events.Transactions.CancelTransactionCommand;
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
        UUID correlationId = UUID.fromString(createTransactionCommand.getCorrelationId());
        // A redelivered CREATE_TRANSACTION_CMD must not record the same transaction twice -
        // correlationId is generated once per saga and stable across retries.
        var existing = transactionRepository.findByCorrelationId(correlationId);
        if (existing.isPresent()) {
            transactionEventProducer.publishTransactionCreated(createTransactionCommand.getSagaId(), existing.get());
            return;
        }

        Transaction newTransaction = new Transaction(
                correlationId,
                createTransactionCommand.getAccountNumber(),
                Long.parseLong(createTransactionCommand.getCustomerId()),
                null,
                createTransactionCommand.getDescription(),
                new BigDecimal(createTransactionCommand.getAmount()),
                createTransactionCommand.getTransactionType());
        newTransaction.setTransactionState(TransactionState.TRAN_COMPLETED);

        transactionRepository.save(newTransaction);

        transactionEventProducer.publishTransactionCreated(createTransactionCommand.getSagaId(), newTransaction);
    }

    @Override
    public void transfer(String sagaId, TransferCommand transferCommand) {
        UUID correlationId = UUID.fromString(transferCommand.getCorrelationId());
        // Same redelivery guard as createTransaction() above.
        var existing = transactionRepository.findByCorrelationId(correlationId);
        if (existing.isPresent()) {
            transactionEventProducer.publishTransactionCreated(sagaId, existing.get());
            return;
        }

        Transaction newTransaction = new Transaction(
                correlationId,
                transferCommand.getSourceAccountNumber(),
                Long.parseLong(transferCommand.getCustomerId()),
                transferCommand.getDestinationAccountNumber(),
                transferCommand.getDescription(),
                new BigDecimal(transferCommand.getAmount()),
                TransactionType.TRANSFER);
        newTransaction.setTransactionState(TransactionState.TRAN_COMPLETED);

        transactionRepository.save(newTransaction);

        transactionEventProducer.publishTransactionCreated(sagaId, newTransaction);
    }

    @Override
    public TransactionDTO retrieveTransactionById(UUID id) {
        Transaction existingTransaction = transactionRepository.findById(id).orElse(null);

        if (existingTransaction == null) return null;
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
    public Page<TransactionDTO> findAllByCustomerId(Long customerId, Pageable pageable) {
        return transactionRepository
                .findAllByCustomerId(customerId, pageable)
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

    @Override
    public void cancelTransactionByCorrelationId(CancelTransactionCommand cancelTransactionCommand) {
        UUID correlationId = UUID.fromString(cancelTransactionCommand.getCorrelationId());
        Transaction existingTransaction = transactionRepository.findByCorrelationId(correlationId).orElse(null);
        // Already cancelled (a redelivered CANCEL_TRANSACTION_CMD) - still confirm back to the
        // orchestrator so a saga waiting on TRANSACTION_CANCELLED_EVENTS_TOPIC isn't left hanging.
        if (existingTransaction != null && existingTransaction.getTransactionState() != TransactionState.TRAN_CANCELLED) {
            existingTransaction.setTransactionState(TransactionState.TRAN_CANCELLED);
            transactionRepository.save(existingTransaction);
        }
        transactionEventProducer.publishTransactionCancelled(cancelTransactionCommand.getSagaId(), correlationId);
    }
}
