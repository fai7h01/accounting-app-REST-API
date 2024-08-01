package com.cydeo.service;

import com.cydeo.dto.UserDto;

import javax.ws.rs.core.Response;

public interface KeycloakService {

    Response userCreate(UserDto dto);
    void delete(String username);
    UserDto getLoggedInUser();

}
