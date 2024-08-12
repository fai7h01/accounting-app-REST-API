package com.cydeo.controller;

import com.cydeo.dto.ProductDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllProducts(){
        List<ProductDto> products = productService.listProductsByCategoryAndName();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product list is successfully retrieved.")
                .data(products).build());
    }

}
