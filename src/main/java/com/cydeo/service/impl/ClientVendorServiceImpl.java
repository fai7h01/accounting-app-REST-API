package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.*;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }


    @Override
    public List<ClientVendorDto> listAllClientVendorsForLoggedInCompany() {
        Long companyId = getLoggedInCompany().getId();
        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByCompany_Id(companyId);
        return clientVendorList.stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto())).toList();
    }

    @Override
    public ClientVendorDto save(ClientVendorDto clientVendorDto) {
        clientVendorDto.setCompany(getLoggedInCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(Long id, ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Client/Vendor not found."));
        clientVendorDto.setId(clientVendor.getId());
        clientVendorDto.setCompany(getLoggedInCompany());
        clientVendorDto.getAddress().setId(clientVendor.getAddress().getId());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        ClientVendor saved = clientVendorRepository.save(updatedClientVendor);
        return mapperUtil.convert(saved, new ClientVendorDto());
    }

    @Override
    public void delete(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Client/Vendor with id: " + id + " not found."));
        clientVendor.setIsDeleted(true);
        clientVendorRepository.save(clientVendor);
    }

    private CompanyDto getLoggedInCompany(){
        return companyService.getCompanyDtoByLoggedInUser();
    }
}