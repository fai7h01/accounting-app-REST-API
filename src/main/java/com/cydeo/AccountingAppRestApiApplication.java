package com.cydeo;

import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountingAppRestApiApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(AccountingAppRestApiApplication.class, args);

        System.out.println("--------------------------------json-----------------------------------------------------");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstname("Luka");
        userDto.setLastname("Varsimashvili");
        userDto.setRole(new RoleDto(1L,  "Admin"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        System.out.println(jsonString);


    }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
