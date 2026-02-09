package com.demobanking.service;

import com.demobanking.entity.AccountSagaState;
import com.demobanking.request.AccountRequest;

public interface IAccountOrchestratorService {
    AccountSagaState initiateAccountCreation(AccountRequest accountRequest);
}
