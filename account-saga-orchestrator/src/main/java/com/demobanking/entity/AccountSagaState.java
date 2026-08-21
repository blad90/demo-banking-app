package com.demobanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_saga_states")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountSagaState {
    @Id
    private String sagaId;
    private String accountNumber;
    private String accountType;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private AccountSagaStatus accountSagaStatus;
    @Enumerated(EnumType.STRING)
    private AccountSagaStep currentStep;
    // Optional, client-supplied - lets a retried/duplicated POST resolve back to the same saga
    // instead of starting a second one. Postgres allows multiple NULLs under a UNIQUE constraint,
    // so requests that don't supply a key are never deduplicated against each other.
    @Column(unique = true)
    private String idempotencyKey;

    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public AccountSagaState(String sagaId, String accountNumber, String accountType, Long userId, AccountSagaStatus accountSagaStatus, AccountSagaStep currentStep, String idempotencyKey) {
        this.sagaId = sagaId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.userId = userId;
        this.accountSagaStatus = accountSagaStatus;
        this.currentStep = currentStep;
        this.idempotencyKey = idempotencyKey;
    }
}