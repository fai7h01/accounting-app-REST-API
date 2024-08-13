package com.cydeo.controller;

import com.cydeo.dto.ProductDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProduct(@RequestBody ProductDto product){
        ProductDto productDto = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Product is successfully created.")
                .data(productDto).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper> updateProduct(@PathVariable Long id, @RequestBody ProductDto product){
        ProductDto updatedProduct = productService.update(id, product);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully updated.")
                .data(updatedProduct).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper> deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true).message("Product is successfully deleted.").build());
    }

}
