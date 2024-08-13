package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendorDto> listAllClientVendorsForLoggedInCompany();
    void delete(Long id);
    ClientVendorDto save(ClientVendorDto clientVendorDto);
    ClientVendorDto update(Long id, ClientVendorDto clientVendorDto);
    ClientVendorDto findByName(String name);
}
