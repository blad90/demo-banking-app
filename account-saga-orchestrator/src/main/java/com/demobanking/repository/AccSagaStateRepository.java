package com.demobanking.repository;

import com.demobanking.entity.AccountSagaState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccSagaStateRepository extends JpaRepository<AccountSagaState, String> {
    Optional<AccountSagaState> findByIdempotencyKey(String idempotencyKey);
}
