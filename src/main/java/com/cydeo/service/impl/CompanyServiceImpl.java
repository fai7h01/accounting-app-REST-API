package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exception.CompanyNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.KeycloakService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final KeycloakService keycloakService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, KeycloakService keycloakService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.keycloakService = keycloakService;
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto findByTitle(String title) {
        return mapperUtil.convert(companyRepository.findByTitle(title), new CompanyDto());
    }

    @Override
    public List<CompanyDto> findAllAndSorted() {
        return companyRepository.findAllExcludingRootCompanySortedByStatusAndTitle()
                .stream()
                .map(company -> mapperUtil.convert(company, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(companyDto, new Company());
        Company saved = companyRepository.save(company);
        return mapperUtil.convert(saved, new CompanyDto());
    }

    @Override
    public CompanyDto update(Long id, CompanyDto companyDto) {
        Company foundCompany = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        companyDto.setId(foundCompany.getId());
        companyDto.getAddress().setId(foundCompany.getAddress().getId());
        Company convertedCompany = mapperUtil.convert(companyDto, new Company());
        Company saved = companyRepository.save(convertedCompany);
        return mapperUtil.convert(saved, new CompanyDto());
    }

    @Override
    public void activate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    @Override
    public CompanyDto getCompanyDtoByLoggedInUser() {
        UserDto userDto = keycloakService.getLoggedInUser();
        return userDto.getCompany();
    }

}
