package com.cydeo.service;

import com.cydeo.dto.UserDto;
import com.cydeo.exception.UserDoesNotExistsException;

import javax.ws.rs.core.Response;

public interface KeycloakService {

    Response userCreate(UserDto dto);
    void delete(String username);
    UserDto getLoggedInUser() throws UserDoesNotExistsException;

}
