package com.cydeo.service;

import com.cydeo.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto findById(Long id);
    List<CategoryDto> listCategoryByCompany();
    CategoryDto save(CategoryDto dto);
    void update(CategoryDto dto);
    boolean isDescriptionUnique(Long id, String description, Long excludeCategoryId);
    void delete(Long id);
}
