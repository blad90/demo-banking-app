package com.demobanking.controller;

public record CreateAccountCommand(String accountNumber, Long userId, String accountType) {}
