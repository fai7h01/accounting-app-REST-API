package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.enums.CompanyStatus;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    CompanyDto findByTitle(String title);

    List<CompanyDto> findAllAndSorted(); // root user can list all companies except itself

    CompanyDto save(CompanyDto companyDto);

    CompanyDto update(Long id, CompanyDto companyDto);

    void activate(Long id);

    void deactivate(Long id);

    CompanyDto getCompanyDtoByLoggedInUser();

}
