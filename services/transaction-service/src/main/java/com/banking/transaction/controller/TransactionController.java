package com.banking.transaction.controller;

import com.banking.transaction.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @GetMapping("/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return new TransactionDTO();
    }

    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transaction) {
        return transaction;
    }
}
