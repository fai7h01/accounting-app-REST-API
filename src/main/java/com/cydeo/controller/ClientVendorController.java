package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.ClientVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientVendor")
@RequiredArgsConstructor
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> listAllClientVendors(){
        List<ClientVendorDto> clientVendorDtoList = clientVendorService.listAllClientVendors();
        return null;
    }

}
