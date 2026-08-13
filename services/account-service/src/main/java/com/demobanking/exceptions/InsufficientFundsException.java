package com.demobanking.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountNumber, BigDecimal amount) {
        super("Account No. " + accountNumber + " has insufficient funds to debit " + amount);
    }
}
