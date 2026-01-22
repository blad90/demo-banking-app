package com.banking.transaction.dto;

import com.banking.transaction.entity.TransactionState;
import com.banking.transaction.entity.TransactionType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDTO {
    private UUID id;
    private String description;
    private double transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private TransactionState transactionState;
}
