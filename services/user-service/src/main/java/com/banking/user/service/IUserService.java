package com.banking.user.service;

import com.banking.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {
    void registerUser();
    void updateUser();
    void disableUser();
    User retrieveUserById(Long id);
    List<User> retrieveAllUsers();
}
