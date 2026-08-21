package com.demobanking.request;

import com.demobanking.events.Transactions.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long customerId;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
    // Optional. If a request with this key was already processed, the existing saga is
    // returned instead of starting a new one - lets a client safely retry on timeout.
    private String idempotencyKey;
}
