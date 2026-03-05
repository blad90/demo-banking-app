package com.demobanking.repository;

import com.demobanking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountNumber(String accountNumber);
    List<Account> findAllByAccountNumberIn(List<String> accountNumbers);
    Page<Account> findAllByCustomerId(Long customerId, Pageable pageable);
    Page<Account> findAllByAccountNumberContainingIgnoreCase(String accountNumber, Pageable pageable);
}
