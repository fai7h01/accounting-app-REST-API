package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
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

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, @Lazy KeycloakService keycloakService) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }


    @Override
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new IllegalArgumentException("User does not exists");
        }
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public UserDto save(UserDto userDto) {

        if (usernameAlreadyExists(userDto.getUsername())){
            throw new IllegalArgumentException("Username: " + userDto.getUsername() + " already exists");
        }

        //TODO check if password match and exception handling
//        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
//            throw new IllegalArgumentException("Passwords do not match");
//        }

        User entity = userRepository.save(mapperUtil.convert(userDto, new User()));
        keycloakService.userCreate(userDto);
        return mapperUtil.convert(entity, new UserDto());
    }

    private boolean usernameAlreadyExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> user = userRepository.findByUsername(userDto.getUsername());
        User converted = mapperUtil.convert(userDto, new User());
        if (user.isPresent()){
            converted.setId(user.get().getId());
            userRepository.save(converted);
        }
        return mapperUtil.convert(converted, new UserDto());
    }


    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        UserDto userDto = mapperUtil.convert(user, new UserDto());
        userDto.setOnlyAdmin(isOnlyAdmin(userDto));
        return userDto;
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
    public List<UserDto> findByCompanyId(Long companyId) {
        return userRepository.findByCompany_Id(companyId).stream()
                .map(user -> mapperUtil.convert(user, new UserDto()))
                .sorted(Comparator.comparing(UserDto::getRoleDescription))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByUsername(email).isPresent();
    }


    @Override
    public boolean isOnlyAdmin(UserDto userDto) {
        return userRepository.isOnlyAdminInCompany(userDto.getCompany().getId());
    }


    @Override
    public boolean isPasswordMatched(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    @Override
    public List<UserDto> findAllByRoleDescription(String role) {
        return userRepository.findAllByRoleDescription(role).stream()
                .map(user -> {
                    UserDto userDto = mapperUtil.convert(user, new UserDto());
                    userDto.setOnlyAdmin(isOnlyAdmin(userDto));
                    return userDto;
                })
                .sorted(Comparator.comparing(UserDto::getCompanyName)
                        .thenComparing(UserDto::getRoleDescription))
                .collect(Collectors.toList());
    }

}
