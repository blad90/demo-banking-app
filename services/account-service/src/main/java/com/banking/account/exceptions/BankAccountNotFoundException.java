package com.banking.account.exceptions;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String accountNumber) {
        super("Account No. " + accountNumber + " not found!");
    }
}
