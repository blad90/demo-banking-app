package com.demobanking.entity;

import com.demobanking.events.Transactions.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_saga_states")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionSagaState {
    @Id
    private String sagaId;
    private String correlationId;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String transactionDescription;
    @Enumerated(EnumType.STRING)
    private TransactionSagaStatus transactionSagaStatus;
    @Enumerated(EnumType.STRING)
    private TransactionSagaStep currentStep;

    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public TransactionSagaState(String sagaId, String correlationId, String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount, TransactionType transactionType, String transactionDescription, TransactionSagaStatus transactionSagaStatus, TransactionSagaStep currentStep) {
        this.sagaId = sagaId;
        this.correlationId = correlationId;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDescription = transactionDescription;
        this.transactionSagaStatus = transactionSagaStatus;
        this.currentStep = currentStep;
    }
}