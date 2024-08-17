package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;
    private final ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.productService = productService;
    }


    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        CategoryDto dto = mapperUtil.convert(category, new CategoryDto());
        dto.setHasProduct(!category.getProducts().isEmpty());
        return dto;
    }

    @Override
    public CategoryDto findByDescription(String desc) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        Category category = categoryRepository.findByDescriptionAndCompanyId(desc, companyId).orElseThrow(() -> new NoSuchElementException("Category not found"));
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public List<CategoryDto> listCategoryByCompany() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return categoryRepository.findByCompanyIdOrderByDescriptionAsc(companyId).stream()
                .map(category -> {
                    CategoryDto categoryDto = mapperUtil.convert(category, new CategoryDto());
                    List<ProductDto> products = productService.findAllByCategoryAndCompany(category);
                    categoryDto.setHasProduct(!products.isEmpty());
                    return categoryDto;
                })
                .toList();
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        if (categoryAlreadyExists(dto)){
            throw new RuntimeException("Category already exists.");
        }
        CompanyDto logged = companyService.getCompanyDtoByLoggedInUser();
        dto.setCompany(logged);
        Category saved = categoryRepository.save(mapperUtil.convert(dto, new Category()));
        return mapperUtil.convert(saved, new CategoryDto());
    }

    private boolean categoryAlreadyExists(CategoryDto dto){
        CompanyDto loggedCompany = companyService.getCompanyDtoByLoggedInUser();
        Optional<Category> category = categoryRepository.findByDescriptionAndCompanyId(dto.getDescription(), loggedCompany.getId());
        return category.isPresent();
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found."));
        Category converted = mapperUtil.convert(dto, new Category());
        converted.setId(foundCategory.getId());
        converted.setCompany(foundCategory.getCompany());
        Category saved = categoryRepository.save(converted);
        return mapperUtil.convert(saved, new CategoryDto());
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found."));
        if (!category.getProducts().isEmpty()) throw new RuntimeException("Category has products, it can not be deleted.");
        categoryRepository.delete(category);
    }

}
