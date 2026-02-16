package com.banking.transaction.dto;

import com.demobanking.events.Transactions.TransactionType;
import com.demobanking.events.Transactions.TransactionState;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDTO {
    private UUID id;
    private UUID correlationId;
    private String sourceAccount;
    private String destinationAccount;
    private String description;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private TransactionState transactionState;
}
