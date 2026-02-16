package com.banking.transaction.entity;

import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.events.Transactions.TransactionState;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_state")
    private TransactionState transactionState;

    public Transaction(UUID correlationId, String sourceAccountNumber,
                       String destinationAccountNumber, String description,
                       BigDecimal transactionAmount, TransactionType type) {
        this.correlationId = correlationId;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.type = type;
    }
}
