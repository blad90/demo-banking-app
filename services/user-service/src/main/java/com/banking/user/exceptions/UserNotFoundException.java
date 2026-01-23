package com.banking.user.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("User with ID " + userId + " not found!");
    }
}
