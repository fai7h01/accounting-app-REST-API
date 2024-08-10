package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.KeycloakService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, UserRepository userRepository, KeycloakService keycloakService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(company, new CompanyDto());
    }

//    @Override
//    public List<CompanyDto> listAllCompany() {
//        List<Company> companyList = companyRepository.findAll();
//        return companyList.stream().map(company -> mapperUtil.convert(company, new CompanyDto())).collect(Collectors.toList());
//    }


    @Override
    public List<CompanyDto> findAllAndSorted() {
        return companyRepository.findAllExcludingRootCompanySortedByStatusAndTitle()
                .stream()
                .map(company -> mapperUtil.convert(company, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(companyDto, new Company());
        companyRepository.save(company);
    }

    @Override
    public void update(CompanyDto companyDto) {
        Optional<Company> foundCompany = companyRepository.findById(companyDto.getId());
        Company convertedCompany = mapperUtil.convert(companyDto, new Company());
        if (foundCompany.isPresent()) {
            convertedCompany.setId(foundCompany.get().getId());
            convertedCompany.setCompanyStatus(CompanyStatus.ACTIVE);
            companyRepository.save(convertedCompany);
        }

    }

    @Override
    public CompanyDto getUserCompany() {
        return null;
    }

    @Transactional
    @Override
    public void activateCompany(Long id) {
        Company company= companyRepository.findById(id).orElseThrow(()-> new RuntimeException("Company Not found"));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
        userRepository.updateUserStatusByCompanyId(company.getId(), true);
    }

    @Transactional
    @Override
    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Not found"));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
        userRepository.updateUserStatusByCompanyId(company.getId(), false);
    }

    @Override
    public boolean titleIsExist(String companyTitle) {
    return  companyRepository.findByTitleIs(companyTitle)!=null;
    }

    @Override
    public CompanyDto getCompanyDtoByLoggedInUser() {
        UserDto userDto = keycloakService.getLoggedInUser();
        return userDto.getCompany();
    }

}
