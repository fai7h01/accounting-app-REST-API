package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllUser(){
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .message("Users are successfully retrieved.")
                .data(users).build());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDto user){
        UserDto userDto = userService.save(user);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .message("User is successfully created.")
                .data(userDto).build());
    }
}
