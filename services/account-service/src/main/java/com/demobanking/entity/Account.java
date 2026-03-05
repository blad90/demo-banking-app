package com.demobanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String accountNumber;
    private Long customerId;
    private BigDecimal balance;
    private String accountType;
    @CreationTimestamp
    private LocalDateTime accountCreationDate;
    @UpdateTimestamp
    private LocalDateTime accountLastUpdated;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_state")
    private AccountState accountState;

    public Account(String accountNumber, Long customerId, String accountType, AccountState accountState) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.accountState = accountState;
    }
}
