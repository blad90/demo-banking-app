package com.banking.user.service;

import com.banking.user.dto.UserDTO;
import com.banking.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    void registerUser(UserDTO userDTO);
    Boolean updateUser(Long id, UserDTO userDTO);
    Boolean disableUser(Long id,UserDTO userDTO);
    Optional<UserDTO> retrieveUserById(Long id);
    List<UserDTO> retrieveAllUsers();
}
