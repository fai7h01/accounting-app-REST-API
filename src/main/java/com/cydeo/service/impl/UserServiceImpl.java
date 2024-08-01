package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public void save(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = mapperUtil.convert(userDto, new User());
        userRepository.save(user);
    }

    @Override
    public void update(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        userRepository.save(user);
    }


    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        UserDto userDto = mapperUtil.convert(user, new UserDto());
        userDto.setOnlyAdmin(isOnlyAdmin(userDto));
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> mapperUtil.convert(user, new UserDto()))
                .sorted(Comparator.comparing(UserDto::getCompanyName)
                        .thenComparing(UserDto::getRoleDescription))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findByCompanyId(Long companyId) {
        return userRepository.findByCompany_Id(companyId).stream()
                .map(user -> mapperUtil.convert(user, new UserDto()))
                .sorted(Comparator.comparing(UserDto::getRoleDescription))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByUsername(email) != null;
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
