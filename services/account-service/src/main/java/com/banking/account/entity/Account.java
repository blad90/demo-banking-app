package com.banking.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
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
    private String user;
    private double balance;
    private String accountType;
    @CreationTimestamp
    private LocalDateTime accountCreationDate;
    @UpdateTimestamp
    private LocalDateTime accountLastUpdated;
    private AccountState accountState;
}
