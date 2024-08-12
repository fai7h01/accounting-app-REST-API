package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllCategories(){
        List<CategoryDto> categoryDtoList = categoryService.listCategoryByCompany();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Category list is successfully retrieved")
                .data(categoryDtoList).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createCategory(@RequestBody CategoryDto category){
        CategoryDto categoryDto = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Category is successfully created.")
                .data(categoryDto).build());
    }

}
