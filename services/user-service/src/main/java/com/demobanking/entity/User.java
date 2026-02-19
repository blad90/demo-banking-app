package com.demobanking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String alias;
    private String nationalId;
    private String address;
    private String phoneNumber;
    private String emailAddress;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private UserState userState;
    private String userSessionId;
}
