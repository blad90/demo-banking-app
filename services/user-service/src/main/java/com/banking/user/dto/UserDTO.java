package com.banking.user.dto;
import com.banking.user.entity.UserState;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String alias;
    private String address;
    private String phoneNumber;
    private String emailAddress;
    private UserState userState;
}
