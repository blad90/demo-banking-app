package com.demobanking.controller;

import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.request.TransactionRequest;
import com.demobanking.service.ITransactionOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
@CrossOrigin
public class TransactionOrchestratorController {

    private final ITransactionOrchestratorService transactionOrchestratorService;


    @PostMapping("/processTransaction")
    public ResponseEntity<?> startTransaction(@RequestBody TransactionRequest transactionRequest){
        transactionOrchestratorService.initiateTransaction(transactionRequest);

        return ResponseEntity.accepted().body(
                Map.of("status", TransactionState.TRAN_INITIATED)
        );
    }
}
