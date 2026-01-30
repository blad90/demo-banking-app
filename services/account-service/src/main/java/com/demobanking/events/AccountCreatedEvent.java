package com.demobanking.events;

import com.demobanking.entity.AccountState;

public record AccountCreatedEvent(String accountNumber, Long userId, AccountState accountState) {}
