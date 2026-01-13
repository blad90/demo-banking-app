package com.banking.transaction.repository;

import com.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
}
