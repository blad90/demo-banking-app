package com.demobanking.repository;

import com.demobanking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(UUID transactionId);
    Page<Transaction> findAllByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    Page<Transaction> findAllByCustomerId(Long customerId, Pageable pageable);
}
