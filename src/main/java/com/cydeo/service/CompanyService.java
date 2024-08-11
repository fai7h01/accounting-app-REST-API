package com.cydeo.service;

import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    CompanyDto findByTitle(String title);

    //List<CompanyDto> listAllCompany();

    List<CompanyDto> findAllAndSorted(); // root user can list all companies except itself

    CompanyDto save(CompanyDto companyDto);

    CompanyDto update(Long id, CompanyDto companyDto);

//
//    void activateCompany(Long id);
//
//    void deactivateCompany(Long id);
//
// //   void activateOrDeactivateUsers(Company company, boolean status);
//
//    boolean titleIsExist(String companyTitle);
//
    CompanyDto getCompanyDtoByLoggedInUser();

}
