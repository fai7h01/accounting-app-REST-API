package com.cydeo.service;

import com.cydeo.dto.CategoryDto;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryService {
    CategoryDto findById(Long id);
    List<CategoryDto> listCategoryByCompany();
    CategoryDto save(CategoryDto dto);
    CategoryDto update(Long id, CategoryDto dto);
    boolean isDescriptionUnique(Long id, String description, Long excludeCategoryId);
    @Transactional
    void delete(Long id);
}
