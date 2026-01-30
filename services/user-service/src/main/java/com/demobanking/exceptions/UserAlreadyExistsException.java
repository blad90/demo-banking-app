package com.demobanking.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String userNationalId) {
        super("There is already an user with National ID " + userNationalId + "!");
    }
}
