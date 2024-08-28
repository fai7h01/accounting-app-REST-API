package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(description = "Category Controller", name = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "List all categories")
    public ResponseEntity<ResponseWrapper> listAllCategories(){
        List<CategoryDto> categoryDtoList = categoryService.listCategoryByCompany();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Category list is successfully retrieved.")
                .data(categoryDtoList).build());
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<ResponseWrapper> createCategory(@RequestBody CategoryDto category){
        CategoryDto categoryDto = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Category is successfully created.")
                .data(categoryDto).build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<ResponseWrapper> updateCategory(@PathVariable Long id, @RequestBody CategoryDto category){
        CategoryDto updated = categoryService.update(id, category);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Category is successfully updated.")
                .data(updated).build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<ResponseWrapper> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Category is successfully deleted.").build());
    }

}
