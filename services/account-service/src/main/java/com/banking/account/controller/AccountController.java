package com.banking.account.controller;

import com.banking.account.dto.AccountDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @GetMapping("/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return new AccountDTO();
    }

    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountDTO account) {
        return account;
    }
}
