package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.UserDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(description = "User Controller", name = "User API")
public class UserController {

    private final UserService userService;

    @ExecutionTime
    @RolesAllowed({"Root User", "Admin"})
    @GetMapping("/list")
    @Operation(summary = "List all users")
    public ResponseEntity<ResponseWrapper> listAllUser(){
        List<UserDto> users = userService.getAllByLoggedInUser();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Users are successfully retrieved.")
                .data(users).build());
    }

    @RolesAllowed({"Root User"})
    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDto user){ //@valid
        UserDto userDto = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder().code(HttpStatus.CREATED.value())
                .success(true)
                .message("User is successfully created.")
                .data(userDto).build());
    }

    //@RolesAllowed({"Root User"})
    @PutMapping("/update/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper> updateUser(@PathVariable Long id, @RequestBody UserDto user){
        UserDto userDto = userService.update(id, user);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("User is successfully updated.")
                .data(userDto).build());
    }

    @RolesAllowed({"Root User"})
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseWrapper> deleteUserById(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("User is successfully deleted.")
                .build());
    }
}
