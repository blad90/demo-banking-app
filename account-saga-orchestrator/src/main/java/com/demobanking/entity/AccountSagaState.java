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

    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public AccountSagaState(String sagaId, String accountNumber, String accountType, Long userId, AccountSagaStatus accountSagaStatus, AccountSagaStep currentStep) {
        this.sagaId = sagaId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.userId = userId;
        this.accountSagaStatus = accountSagaStatus;
        this.currentStep = currentStep;
    }
}