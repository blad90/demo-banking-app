package com.banking.transaction.dto;

import com.banking.transaction.entity.TransactionState;
import com.banking.transaction.entity.TransactionType;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDTO {
    private Long id;
    private String description;
    private double transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private TransactionState transactionState;
}
