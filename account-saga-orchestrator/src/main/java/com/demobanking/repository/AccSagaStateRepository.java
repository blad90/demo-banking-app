package com.demobanking.repository;

import com.demobanking.entity.AccountSagaState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccSagaStateRepository extends JpaRepository<AccountSagaState, String> {
}
