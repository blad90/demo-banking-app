package com.banking.user.utils;

import com.banking.user.dto.UserDTO;
import com.banking.user.entity.User;
import com.banking.user.entity.UserState;

public class UserMapper {

    public static UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAlias(user.getAlias());
        userDTO.setNationalId(user.getNationalId());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUserState(user.getUserState());
        return userDTO;
    }

    public static User mapToEntity(UserDTO userDTO) {
        User user = new User();
        //user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAlias(userDTO.getAlias());
        user.setNationalId(userDTO.getNationalId());
        user.setAddress(userDTO.getAddress());
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserState(UserState.USER_CREATED);

        return user;
    }

    public static User mapToEntityUpdate(UserDTO userDTO, User user) {
        if(userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if(userDTO.getAlias() != null) user.setAlias(userDTO.getAlias());
        if(userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
        if(userDTO.getEmailAddress() != null) user.setEmailAddress(userDTO.getEmailAddress());
        if(userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());

        return user;
    }
}
