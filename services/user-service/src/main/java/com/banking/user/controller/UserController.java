package com.banking.user.controller;

import com.banking.user.dto.UserDTO;
import com.banking.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.retrieveUserById(id).get();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> usersDTO = userService.retrieveAllUsers();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> modifyUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        String message;
        boolean result = userService.updateUser(id, userDTO);

        if(result) message = "User updated successfully";
        else message = "Error. User couldn't be updated.";

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<String> disableUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        String message;
        boolean result = userService.disableUser(id, userDTO);

        if(result) message = "Completed. User is being disabled";
        else message = "Error. User couldn't be disabled";

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
