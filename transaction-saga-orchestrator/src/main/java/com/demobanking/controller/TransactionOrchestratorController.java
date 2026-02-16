package com.demobanking.controller;

import com.demobanking.request.TransactionRequest;
import com.demobanking.service.ITransactionOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
public class TransactionOrchestratorController {

    private final ITransactionOrchestratorService transactionOrchestratorService;


    @PostMapping("/processTransaction")
    public void startTransaction(@RequestBody TransactionRequest transactionRequest){
        transactionOrchestratorService.initiateTransaction(transactionRequest);
    }
}
