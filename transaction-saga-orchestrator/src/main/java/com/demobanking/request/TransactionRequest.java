package com.demobanking.request;

import com.demobanking.events.Transactions.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
}
