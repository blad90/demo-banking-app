package com.demobanking.entity;

public enum TransactionSagaStatus {
    STARTED, PROCESSING, FAILED, COMPENSATING, COMPLETED
}