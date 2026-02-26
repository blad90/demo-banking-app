package com.demobanking.service;

import com.demobanking.entity.TransactionSagaState;
import com.demobanking.request.TransactionRequest;

public interface ITransactionOrchestratorService {
    String initiateTransaction(TransactionRequest accountRequest);
    TransactionSagaState retrieveSagaStateById(String sagaId);
}
