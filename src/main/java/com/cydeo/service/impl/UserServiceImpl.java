package com.cydeo.service.impl;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.exception.UserAlreadyExistsException;
import com.cydeo.exception.UserDoesNotExistsException;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final CompanyService companyService;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, @Lazy KeycloakService keycloakService, @Lazy CompanyService companyService) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.companyService = companyService;
    }


    @Override
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserDoesNotExistsException("User does not exists.");
        }
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public UserDto save(UserDto userDto) {

        if (usernameAlreadyExists(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username: " + userDto.getUsername() + " already exists.");
        }

        CompanyDto companyDto = companyService.findByTitle(userDto.getCompany().getTitle());
        userDto.getCompany().setId(companyDto.getId());

        User converted = mapperUtil.convert(userDto, new User());
        User saved = userRepository.save(converted);
        keycloakService.userCreate(userDto);
        return mapperUtil.convert(saved, new UserDto());
    }

    private boolean usernameAlreadyExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        userDto.setId(foundUser.getId());
        userDto.setCompany(mapperUtil.convert(foundUser.getCompany(), new CompanyDto()));
        User saved = userRepository.save(mapperUtil.convert(userDto, new User()));
        return mapperUtil.convert(saved, new UserDto());
    }


    @Override
    public UserDto findById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        UserDto userDto = mapperUtil.convert(user, new UserDto());
        userDto.setOnlyAdmin(isOnlyAdmin(userDto));
        return userDto;
    }

    @Override
    public List<UserDto> getAllByLoggedInUser() {
        if (keycloakService.getLoggedInUser().getRole().getDescription().equals("Admin")) {
            return getAllUsers()
                    .stream().filter(userDto -> userDto.getCompany()
                            .getId().equals(keycloakService.getLoggedInUser()
                                    .getCompany().getId())).collect(Collectors.toList());
        } else if (keycloakService.getLoggedInUser().getRole().getDescription().equals("Root User")) {
            return getAllUsers().stream().
                    filter(userDto -> userDto.getRole().getDescription().equals("Admin")).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("No user is available");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> mapperUtil.convert(user, new UserDto()))
                .sorted(Comparator.comparing((UserDto userDto) -> userDto.getCompany().getTitle())
                        .thenComparing(userDto -> userDto.getRole().getDescription()))
                .peek(userDto -> userDto.setOnlyAdmin(isOnlyAdmin(userDto)))
                .toList();
    }


    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setDeleted(true);
        keycloakService.delete(user.getUsername());
        user.setUsername(user.getUsername() + "-" + user.getId());
        userRepository.save(user);
    }


    private boolean isOnlyAdmin(UserDto userDto) {
        if (!userDto.getRole().getDescription().equalsIgnoreCase("admin")) return false;
        int countAdmins = userRepository.countAllByCompanyTitleAndRoleDescription(userDto.getCompany().getTitle(), "Admin");
        return countAdmins == 1;
    }


}
