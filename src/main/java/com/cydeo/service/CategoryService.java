package com.cydeo.service;

import com.cydeo.dto.CategoryDto;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryService {

    CategoryDto findById(Long id);
    CategoryDto findByDescription(String desc);
    List<CategoryDto> listCategoryByCompany();
    CategoryDto save(CategoryDto dto);
    CategoryDto update(Long id, CategoryDto dto);
    @Transactional
    void delete(Long id);
}
