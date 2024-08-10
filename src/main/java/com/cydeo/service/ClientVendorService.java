package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {
    List<ClientVendorDto> listAllClientVendorsForLoggedInCompany();
//    ClientVendorDto findById(Long id);
//    List<ClientVendorDto> listAllClientVendorsByCompany();
//    List<ClientVendorDto> listAllClientVendorsByType(ClientVendorType clientVendorType);
//    void delete(Long id);
    ClientVendorDto save(ClientVendorDto clientVendorDto);
//    void update(ClientVendorDto clientVendorDto);
//    List<ClientVendorType> findAllTypes();
//
//    boolean existsByName(String clientVendorName);
}
