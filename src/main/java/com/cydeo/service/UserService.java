package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> getAllUsers();

    void delete(Long id);

    boolean isOnlyAdmin(UserDto userDto);




}
