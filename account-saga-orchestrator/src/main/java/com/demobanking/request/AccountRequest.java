package com.demobanking.request;

import lombok.Data;

@Data
public class AccountRequest {
    private Long userId;
    private String accountType;
    // Optional. If a request with this key was already processed, the existing saga is
    // returned instead of starting a new one - lets a client safely retry on timeout.
    private String idempotencyKey;
}
