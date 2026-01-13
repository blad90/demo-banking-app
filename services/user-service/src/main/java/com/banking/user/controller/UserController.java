package com.banking.user.controller;

import com.banking.user.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return new UserDTO();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO user) {
        return user;
    }
}
