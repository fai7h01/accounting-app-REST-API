package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    UserDto save(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> getAllUsers();

    void delete(Long id);


}
