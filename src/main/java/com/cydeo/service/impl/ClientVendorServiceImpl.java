package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Address;
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
    //    private final InvoiceService invoiceService;
    private final AddressService addressService;
    private final CompanyService companyService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, AddressService addressService, CompanyService companyService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.addressService = addressService;
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
        if (clientVendorDto.getAddress() != null) {
            Address address = addressService.save(clientVendorDto.getAddress());
            clientVendor.setAddress(address);
        }
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = clientVendorRepository.findByClientVendorNameAndCompanyId(clientVendorDto.getClientVendorName(), getLoggedInCompany().getId())
                .orElseThrow(() -> new NoSuchElementException("Client/Vendor not found."));
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        updatedClientVendor.setId(clientVendor.getId());
        clientVendorRepository.save(updatedClientVendor);
        return mapperUtil.convert(updatedClientVendor, new ClientVendorDto());
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
//
//    @Override
//    public ClientVendorDto findById(Long id) {
//        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow();
//        return mapperUtil.convert(clientVendor, new ClientVendorDto());
//    }
//
//    @Override
//    public List<ClientVendorDto> listAllClientVendorsByCompany() {
//        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
//        return clientVendorRepository.findByCompany_TitleOrderByClientVendorTypeAscClientVendorNameAsc(companyTitle)
//                .stream()
//                .map(clientVendor -> {
//                    ClientVendorDto clientVendorDto = mapperUtil.convert(clientVendor, new ClientVendorDto());
//                    List<InvoiceDto> invoices = invoiceService.listAllByClientVendor(clientVendor);
//                    clientVendorDto.setHasInvoice(!invoices.isEmpty());
//                    return clientVendorDto;
//                })
//                .collect(Collectors.toList());
//    }
//    @Override
//    public List<ClientVendorDto> listAllClientVendorsByType(ClientVendorType clientVendorType) {
//        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
//        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByClientVendorTypeAndCompany_Title(clientVendorType, companyTitle);
//        return clientVendorList.stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto())).collect(Collectors.toList());
//    }
//    @Override
//    public List<ClientVendorType> findAllTypes() {
//        return List.of(ClientVendorType.CLIENT, ClientVendorType.VENDOR);
//
//    }
//
//    @Override
//    public boolean existsByName(String clientVendorName) {
//        return clientVendorRepository.existsByClientVendorName(clientVendorName);
//    }
}