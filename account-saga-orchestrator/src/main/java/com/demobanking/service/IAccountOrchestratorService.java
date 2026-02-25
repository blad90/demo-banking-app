package com.demobanking.service;

import com.demobanking.entity.AccountSagaState;
import com.demobanking.request.AccountRequest;

public interface IAccountOrchestratorService {
    String initiateAccountCreation(AccountRequest accountRequest);
    AccountSagaState retrieveSagaStateById(String sagaId);
}
