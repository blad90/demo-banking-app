package com.demobanking.controller;

import com.demobanking.dto.AccountDTO;
import com.demobanking.events.Accounts.AccountRequest;
import com.demobanking.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest) {
//        CreateAccountCommand createAccountCommand = new CreateAccountCommand(
//                "OB-" + UUID.randomUUID().toString().substring(0,7).toUpperCase(),
//                accountRequest.getUserId(),
//                accountRequest.getAccountType()
//        );
//        accountService.openAccount(createAccountCommand);
        return new ResponseEntity<>("Account creation accepted.", HttpStatus.OK);
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

    @GetMapping("/all/{customerId}")
    public ResponseEntity<List<AccountDTO>> getAllAccountsByCustomerId(@PathVariable String customerId) {
        List<AccountDTO> allAccounts = accountService.retrieveAllAccountsByCustomerId(customerId);
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @PatchMapping("/update/{accountNumber}")
    public ResponseEntity<String> updateAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {

        accountService.updateAccount(accountNumber, accountDTO);
        return new ResponseEntity<>("Account updated successfully!", HttpStatus.OK);
    }

    @PatchMapping("/enable/{accountNumber}")
    public ResponseEntity<String> enableAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {
        accountService.enableAccount(accountNumber, accountDTO);
        return new ResponseEntity<>("Completed. Account is being activated.", HttpStatus.OK);
    }

    @PatchMapping("/freeze/{accountNumber}")
    public ResponseEntity<String> freezeAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {
        accountService.freezeAccount(accountNumber, accountDTO);
        return new ResponseEntity<>("Completed. Account is being frozen.", HttpStatus.OK);
    }

    @PatchMapping("/inactivate/{accountNumber}")
    public ResponseEntity<String> disableAccount(@PathVariable String accountNumber, @RequestBody AccountDTO accountDTO) {
        accountService.disableAccount(accountNumber, accountDTO);
        return new ResponseEntity<>("Completed. Account is being inactivated.", HttpStatus.OK);
    }
}
