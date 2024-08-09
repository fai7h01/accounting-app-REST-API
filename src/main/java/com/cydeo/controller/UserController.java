package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    @RolesAllowed({"Root User", "Admin"})
    public ResponseEntity<ResponseWrapper> listAllUser(){
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Users are successfully retrieved.")
                .data(users).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDto user){
        UserDto userDto = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder().code(HttpStatus.CREATED.value())
                .success(true)
                .message("User is successfully created.")
                .data(userDto).build());
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDto user){
        UserDto userDto = userService.update(user);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("User is successfully updated.")
                .data(userDto).build());
    }
}
