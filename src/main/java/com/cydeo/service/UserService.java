package com.cydeo.service;

import com.cydeo.dto.UserDto;
import com.cydeo.exception.UserAlreadyExistsException;
import com.cydeo.exception.UserDoesNotExistsException;
import com.cydeo.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    UserDto save(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> getAllByLoggedInUser();

    List<UserDto> getAllUsers();

    void delete(Long id);


}
