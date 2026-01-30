package com.demobanking.service;

import com.demobanking.dto.UserDTO;
import com.demobanking.entity.UserState;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    void registerUser(UserDTO userDTO);
    void updateUser(Long id, UserDTO userDTO);
    void enableUser(Long id, UserDTO userDTO);
    void suspendUser(Long id, UserDTO userDTO);
    void disableUser(Long id,UserDTO userDTO);
    void changeUserState(Long id, UserDTO userDTO, UserState userState);
    void registerLogActivity(Long userId); // TODO: for simplicity and placeholder
    void reverseLogActivity(Long userId); // TODO: for simplicity and placeholder
    Optional<UserDTO> retrieveUserById(Long id);
    List<UserDTO> retrieveAllUsers();
}
