package com.demobanking.request;

import lombok.Data;

@Data
public class AccountRequest {
    private Long userId;
    private String accountType;
}
