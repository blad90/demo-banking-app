package com.demobanking.dto;
import com.demobanking.entity.UserState;
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
    private String nationalId;
    private String address;
    private String phoneNumber;
    private String emailAddress;
    private UserState userState;
    private UserSessionDTO userSessionDTO;
    private String userSessionId;

    public UserDTO(String firstName, String lastName, String alias, String nationalId,
                   String address, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.nationalId = nationalId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UserDTO(Long id, String firstName, String lastName, String alias, String nationalId, String address, String phoneNumber, String emailAddress, UserState userState, String userSessionId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.nationalId = nationalId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.userState = userState;
        this.userSessionId = userSessionId;
    }
}
