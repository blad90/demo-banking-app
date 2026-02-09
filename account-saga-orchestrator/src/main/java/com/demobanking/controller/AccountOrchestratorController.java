package com.demobanking.controller;

import com.demobanking.request.AccountRequest;
import com.demobanking.service.IAccountOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
public class AccountOrchestratorController {

    private final IAccountOrchestratorService accountOrchestratorService;


    @PostMapping("/processAccountCreation")
    public void startAccountCreation(@RequestBody AccountRequest accountRequest){
        accountOrchestratorService.initiateAccountCreation(accountRequest);
    }
}
