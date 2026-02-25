package com.demobanking.controller;

import com.demobanking.entity.AccountSagaState;
import com.demobanking.entity.AccountSagaStatus;
import com.demobanking.request.AccountRequest;
import com.demobanking.service.IAccountOrchestratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orchestrator")
@AllArgsConstructor
@CrossOrigin
public class AccountOrchestratorController {

    private final IAccountOrchestratorService accountOrchestratorService;

    @PostMapping("/processAccountCreation")
    public ResponseEntity<?> startAccountCreation(@RequestBody AccountRequest accountRequest){
        String sagaId = accountOrchestratorService.initiateAccountCreation(accountRequest);

        return ResponseEntity.accepted().body(
                Map.of("sagaId", sagaId,
                        "status", AccountSagaStatus.STARTED)
        );
    }

    @GetMapping("/saga/{sagaId}")
    public ResponseEntity<?> getSagaStatus(@PathVariable String sagaId){
        AccountSagaState accountSagaState = accountOrchestratorService.retrieveSagaStateById(sagaId);

        if (accountSagaState == null) {
            return ResponseEntity.notFound().build();
        }

        return switch (accountSagaState.getAccountSagaStatus()) {
            case STARTED -> ResponseEntity.status(HttpStatus.ACCEPTED).body(accountSagaState);
            case PROCESSING -> ResponseEntity.ok(accountSagaState);
            case FAILED -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(accountSagaState);
            case COMPENSATING -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountSagaState);
            case COMPLETED -> ResponseEntity.ok().body(accountSagaState);
        };
    }
}
