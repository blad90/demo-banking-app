package com.demobanking.controller;

import com.demobanking.entity.TransactionSagaState;
import com.demobanking.entity.TransactionSagaStatus;
import com.demobanking.request.TransactionRequest;
import com.demobanking.service.ITransactionOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        String sagaId = transactionOrchestratorService.initiateTransaction(transactionRequest);

        return ResponseEntity.accepted().body(
                Map.of("sagaId", sagaId,
                        "status", TransactionSagaStatus.STARTED)
        );
    }

    @GetMapping("/saga/{sagaId}")
    public ResponseEntity<?> getSagaStatus(@PathVariable String sagaId){
        TransactionSagaState transactionSagaState = transactionOrchestratorService.retrieveSagaStateById(sagaId);

        if (transactionSagaState == null) {
            return ResponseEntity.notFound().build();
        }

        return switch (transactionSagaState.getTransactionSagaStatus()) {
            case STARTED -> ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionSagaState);
            case PROCESSING -> ResponseEntity.ok(transactionSagaState);
            case FAILED -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(transactionSagaState);
            case COMPENSATING -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(transactionSagaState);
            case COMPLETED -> ResponseEntity.ok().body(transactionSagaState);
        };
    }
}
