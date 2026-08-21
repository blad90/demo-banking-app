package com.demobanking.repository;

import com.demobanking.entity.TransactionSagaState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransacSagaStateRepository extends JpaRepository<TransactionSagaState, String> {
    Optional<TransactionSagaState> findByIdempotencyKey(String idempotencyKey);
}
