package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    void save(UserDto userDto);

    void update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> getAllUsers();

    List<UserDto> findByCompanyId(Long companyId);

    void deleteUser(Long id);

    boolean emailExists(String email);

    boolean isOnlyAdmin(UserDto userDto);


    boolean isPasswordMatched(String password, String confirmPassword);

    List<UserDto> findAllByRoleDescription(String role);





}
