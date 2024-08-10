package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.ClientVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientVendor")
@RequiredArgsConstructor
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllClientVendors(){
        List<ClientVendorDto> clientVendorDtoList = clientVendorService.listAllClientVendorsForLoggedInCompany();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Client/Vendor list is successfully retrieved.")
                .data(clientVendorDtoList).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createClientVendor(@RequestBody ClientVendorDto clientVendor){
        ClientVendorDto clientVendorDto = clientVendorService.save(clientVendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message(clientVendorDto.getClientVendorType().getValue() + " is successfully created.")
                .data(clientVendorDto).build());
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateClientVendor(@RequestBody ClientVendorDto clientVendor){
        ClientVendorDto updatedClientVendor = clientVendorService.update(clientVendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message(updatedClientVendor.getClientVendorType().getValue() + " is successfully updated.")
                .data(updatedClientVendor).build());
    }

}
