package com.cydeo.service.impl;


import com.cydeo.dto.AddressDto;
import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Address;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.*;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceService invoiceService;
    private final AddressService addressService;
    private final CompanyService companyService;

    public ClientVendorServiceImpl(AddressService addressService, CompanyService companyService,ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, InvoiceService invoiceService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.addressService = addressService;
        this.invoiceService = invoiceService;
        this.companyService = companyService;
    }

    @Override
    public List<ClientVendorDto> listAllClientVendors() {
        List<ClientVendor> clientVendorList = clientVendorRepository.findAll();
        return clientVendorList.stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto())).collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto save(ClientVendorDto clientVendorDto) {
        clientVendorDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        if (clientVendorDto.getAddress() != null) {
            Address address = addressService.save(clientVendorDto.getAddress());
            clientVendor.setAddress(address);
        }
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public void update(ClientVendorDto clientVendorDto) {
        ClientVendor existingClientVendor = clientVendorRepository.findById(clientVendorDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client/Vendor not found"));

        existingClientVendor.setClientVendorName(clientVendorDto.getClientVendorName());
        existingClientVendor.setClientVendorType(clientVendorDto.getClientVendorType());
        existingClientVendor.setPhone(clientVendorDto.getPhone());
        existingClientVendor.setWebsite(clientVendorDto.getWebsite());

        if (clientVendorDto.getAddress() != null) {
            Address existingAddress = existingClientVendor.getAddress();
            AddressDto addressDto = clientVendorDto.getAddress();

            if (existingAddress != null) {
                existingAddress.setAddressLine1(addressDto.getAddressLine1());
                existingAddress.setAddressLine2(addressDto.getAddressLine2());
                existingAddress.setCity(addressDto.getCity());
                existingAddress.setState(addressDto.getState());
                existingAddress.setCountry(addressDto.getCountry());
                existingAddress.setZipCode(addressDto.getZipCode());

                AddressDto updatedAddressDto = mapperUtil.convert(existingAddress, new AddressDto());
                addressService.save(updatedAddressDto);
            } else {
                Address newAddress = mapperUtil.convert(addressDto, new Address());
                AddressDto newAddressDto = mapperUtil.convert(newAddress, new AddressDto());
                Address savedNewAddress = mapperUtil.convert(addressService.save(newAddressDto), new Address());
                existingClientVendor.setAddress(savedNewAddress);
            }
        }
        clientVendorRepository.save(existingClientVendor);
    }

    @Override
    public ClientVendorDto findById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow();
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> listAllClientVendorsByCompany() {
        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
        return clientVendorRepository.findByCompany_TitleOrderByClientVendorTypeAscClientVendorNameAsc(companyTitle)
                .stream()
                .map(clientVendor -> {
                    ClientVendorDto clientVendorDto = mapperUtil.convert(clientVendor, new ClientVendorDto());
                    List<InvoiceDto> invoices = invoiceService.listAllByClientVendor(clientVendor);
                    clientVendorDto.setHasInvoice(!invoices.isEmpty());
                    return clientVendorDto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<ClientVendorDto> listAllClientVendorsByType(ClientVendorType clientVendorType) {
        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByClientVendorTypeAndCompany_Title(clientVendorType, companyTitle);
        return clientVendorList.stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto())).collect(Collectors.toList());
    }
    @Override
    public void delete(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        clientVendor.setIsDeleted(true);
        clientVendorRepository.save(clientVendor);
    }
    @Override
    public List<ClientVendorType> findAllTypes() {
        return List.of(ClientVendorType.CLIENT, ClientVendorType.VENDOR);

    }

    @Override
    public boolean existsByName(String clientVendorName) {
        return clientVendorRepository.existsByClientVendorName(clientVendorName);
    }
}