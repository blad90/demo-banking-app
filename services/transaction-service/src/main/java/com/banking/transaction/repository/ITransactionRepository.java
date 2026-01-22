package com.banking.transaction.repository;

import com.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(UUID transactionId);
}
