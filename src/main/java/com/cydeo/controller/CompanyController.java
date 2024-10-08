package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Company Controller", description = "Company API")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/list")
    @Operation(summary = "List all companies")
    public ResponseEntity<ResponseWrapper> listAllCompanies(){
        List<CompanyDto> companyDtoList = companyService.findAllAndSorted();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Company list is successfully retrieved.")
                .data(companyDtoList).build());
    }

    @PostMapping
    @Operation(summary = "Create company")
    public ResponseEntity<ResponseWrapper> createCompany(@RequestBody CompanyDto company){
        CompanyDto companyDto = companyService.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Company is successfully created")
                .data(companyDto).build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update company")
    public ResponseEntity<ResponseWrapper> updateCompany(@PathVariable Long id, @RequestBody CompanyDto company){
        CompanyDto updatedCompany = companyService.update(id, company);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("Company is successfully updated.")
                .data(updatedCompany).build());
    }

    @GetMapping("/activate/{id}")
    @Operation(summary = "Activate company")
    public ResponseEntity<ResponseWrapper> activateCompany(@PathVariable Long id){
        companyService.activate(id);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("Company is successfully activated.").build());
    }

    @GetMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate company")
    public ResponseEntity<ResponseWrapper> deactivateCompany(@PathVariable Long id){
        companyService.deactivate(id);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("Company is successfully deactivated.").build());
    }

}
