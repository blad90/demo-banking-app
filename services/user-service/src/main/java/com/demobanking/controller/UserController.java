package com.demobanking.controller;

import com.demobanking.dto.UserDTO;
import com.demobanking.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private IUserService userService;
    private final KafkaTemplate<String, Object> template;

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
        userService.updateUser(id, userDTO);
        return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<String> enableUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.enableUser(id, userDTO);
        return new ResponseEntity<>("Completed. User is being activated.", HttpStatus.OK);
    }

    @PatchMapping("/suspend/{id}")
    public ResponseEntity<String> suspendUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.suspendUser(id, userDTO);
        return new ResponseEntity<>("Completed. User is being suspended.", HttpStatus.OK);
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<String> disableUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.disableUser(id, userDTO);
        return new ResponseEntity<>("Completed. User is being inactivated.", HttpStatus.OK);
    }
}
