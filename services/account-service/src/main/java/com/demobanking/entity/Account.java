package com.demobanking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
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
    private Long customer;
    private Double balance;
    private String accountType;
    @CreationTimestamp
    private LocalDateTime accountCreationDate;
    @UpdateTimestamp
    private LocalDateTime accountLastUpdated;
    private AccountState accountState;

    public Account(String accountNumber, Long customer, String accountType, AccountState accountState) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.accountType = accountType;
        this.accountState = accountState;
    }
}
