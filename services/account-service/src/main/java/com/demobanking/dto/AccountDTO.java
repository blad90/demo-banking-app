package com.demobanking.dto;

import com.demobanking.entity.AccountState;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Long customer;
    private BigDecimal balance;
    private String accountType;
    private LocalDateTime accountCreationDate;
    private LocalDateTime accountLastUpdated;
    private AccountState accountState;

    public AccountDTO(String accountNumber, String accountType, AccountState accountState) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountState = accountState;
    }
}
