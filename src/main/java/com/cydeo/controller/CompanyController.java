package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ResponseWrapper> createCompany(@RequestBody CompanyDto company){
        CompanyDto companyDto = companyService.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Company is successfully created")
                .data(companyDto).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper> updateCompany(@PathVariable Long id, @RequestBody CompanyDto company){
        CompanyDto updatedCompany = companyService.update(id, company);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("Company is successfully updated.")
                .data(updatedCompany).build());
    }

}
