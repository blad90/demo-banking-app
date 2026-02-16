package com.banking.transaction.controller;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.service.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private ITransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID id) {
        TransactionDTO transactionDTO = transactionService.retrieveTransactionById(id);
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactionsDTO = transactionService.retrieveAllTransactions();
        return new ResponseEntity<>(transactionsDTO, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelTransaction(@PathVariable UUID id) {
        transactionService.cancelTransaction(id);
        return new ResponseEntity<>("Transaction cancelled", HttpStatus.OK);
    }


}
