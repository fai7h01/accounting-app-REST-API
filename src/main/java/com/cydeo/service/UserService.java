package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> getAllUsers();

    List<UserDto> findByCompanyId(Long companyId);

    void delete(Long id);

    boolean emailExists(String email);

    boolean isOnlyAdmin(UserDto userDto);


    boolean isPasswordMatched(String password, String confirmPassword);

    List<UserDto> findAllByRoleDescription(String role);





}
