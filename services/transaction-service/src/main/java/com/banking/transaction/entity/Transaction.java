package com.banking.transaction.entity;

import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.events.Transactions.TransactionState;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID correlationId;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String description;
    private BigDecimal transactionAmount;
    @CreationTimestamp
    private LocalDateTime transactionDate;
    private TransactionType type;
    private TransactionState transactionState;

    public Transaction(UUID correlationId, String sourceAccountNumber, String destinationAccountNumber, String description, BigDecimal transactionAmount) {
        this.correlationId = correlationId;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.description = description;
        this.transactionAmount = transactionAmount;
    }
}
