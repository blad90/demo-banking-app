package com.demobanking.controller;

import com.demobanking.service.IAccountOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
public class AccountOrchestratorController {

    private final IAccountOrchestratorService accountOrchestratorService;

    public void createAccount(){

    }
}
