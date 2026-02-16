package com.demobanking.repository;

import com.demobanking.entity.TransactionSagaState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacSagaStateRepository extends JpaRepository<TransactionSagaState, String> {
}
