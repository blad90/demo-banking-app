package com.banking.user.service;

import com.banking.user.dto.UserDTO;
import com.banking.user.entity.User;
import com.banking.user.entity.UserState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    void registerUser(UserDTO userDTO);
    void updateUser(Long id, UserDTO userDTO);
    void enableUser(Long id, UserDTO userDTO);
    void suspendUser(Long id, UserDTO userDTO);
    void disableUser(Long id,UserDTO userDTO);
    void changeUserState(Long id, UserDTO userDTO, UserState userState);
    Optional<UserDTO> retrieveUserById(Long id);
    List<UserDTO> retrieveAllUsers();
}
