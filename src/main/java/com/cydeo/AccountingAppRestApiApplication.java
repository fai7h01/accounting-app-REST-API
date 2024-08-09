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
    }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
