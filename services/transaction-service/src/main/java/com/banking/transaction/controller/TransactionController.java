package com.banking.transaction.controller;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.service.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/page/all")
    public ResponseEntity<Page<TransactionDTO>> getAllAccountsPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String transactionDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(transactionDate));
        Page<TransactionDTO> allAccounts = transactionService.findAllTransactions(pageable);
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @GetMapping("/page/all/search")
    public ResponseEntity<Page<TransactionDTO>> getAllAccountsPageable(Pageable pageable, @RequestParam String description) {
        Page<TransactionDTO> allAccounts = transactionService.findAllFilteredTransactions(description, pageable);
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelTransaction(@PathVariable UUID id) {
        transactionService.cancelTransaction(id);
        return new ResponseEntity<>("Transaction cancelled", HttpStatus.OK);
    }


}
