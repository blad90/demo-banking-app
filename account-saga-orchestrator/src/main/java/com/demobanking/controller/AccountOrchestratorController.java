package com.demobanking.controller;

import com.demobanking.AccountState;
import com.demobanking.request.AccountRequest;
import com.demobanking.service.IAccountOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
public class AccountOrchestratorController {

    private final IAccountOrchestratorService accountOrchestratorService;

    @PostMapping("/processAccountCreation")
    public ResponseEntity<?> startAccountCreation(@RequestBody AccountRequest accountRequest){
        accountOrchestratorService.initiateAccountCreation(accountRequest);

        return ResponseEntity.accepted().body(
                Map.of("status", AccountState.ACCOUNT_CREATED));
    }
}
