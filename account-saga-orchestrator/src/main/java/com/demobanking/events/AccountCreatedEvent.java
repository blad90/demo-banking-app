package com.demobanking.events;

import com.demobanking.AccountState;

public record AccountCreatedEvent(String accountNumber, Long userId, AccountState accountState) {}
