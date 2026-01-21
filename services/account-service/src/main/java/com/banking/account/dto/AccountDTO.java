package com.banking.account.dto;

import com.banking.account.entity.AccountState;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private String customer;
    private Double balance;
    private String accountType;
    private LocalDateTime accountCreationDate;
    private LocalDateTime accountLastUpdated;
    private AccountState accountState;
}
