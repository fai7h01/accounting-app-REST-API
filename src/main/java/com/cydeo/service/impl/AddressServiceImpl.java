package com.cydeo.service.impl;

import com.cydeo.dto.AddressDto;
import com.cydeo.entity.Address;
import com.cydeo.repository.AddressRepository;
import com.cydeo.service.AddressService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;

    public AddressServiceImpl(AddressRepository addressRepository, MapperUtil mapperUtil) {
        this.addressRepository = addressRepository;
        this.mapperUtil = mapperUtil;
    }
    @Override
    public Address save(AddressDto addressDto) {
        Address address = mapperUtil.convert(addressDto, new Address());
        return addressRepository.save(address);
    }
}
