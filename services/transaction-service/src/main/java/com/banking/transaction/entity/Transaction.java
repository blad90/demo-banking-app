package com.banking.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private double transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private TransactionState transactionState;
}
