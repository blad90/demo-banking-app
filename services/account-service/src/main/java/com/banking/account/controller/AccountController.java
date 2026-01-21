package com.banking.account.controller;

import com.banking.account.dto.AccountDTO;
import com.banking.account.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO) {
        accountService.openAccount(0L, accountDTO);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String accountNumber) {
        AccountDTO retrievedAccount = accountService.retrieveAccountByAccNumber(accountNumber);
        return new ResponseEntity<>(retrievedAccount, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> allAccounts = accountService.retrieveAllAccounts();
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @PatchMapping("/update/{accountNumber}")
    public ResponseEntity<String> updateAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {
        String message;
        boolean result = accountService.updateAccount(accountNumber, accountDTO);
        if(result) message = "Account updated successfully";
        else message = "Error. Account couldn't be updated.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/inactivate/{accountNumber}")
    public ResponseEntity<String> disableAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {
        String message;
        boolean result = accountService.disableAccount(accountNumber, accountDTO);
        if(result) message = "Complete. The account is being inactivated.";
        else message = "Error. Account couldn't be inactivated.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
