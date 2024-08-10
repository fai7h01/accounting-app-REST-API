package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllCompanies(){
        List<CompanyDto> companyDtoList = companyService.findAllAndSorted();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Company list is successfully retrieved.")
                .data(companyDtoList).build());
    }

}
