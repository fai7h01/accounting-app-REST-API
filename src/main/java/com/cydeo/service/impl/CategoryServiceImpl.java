package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<CategoryDto> listAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> mapperUtil.convert(category, new CategoryDto())).toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        CategoryDto dto = mapperUtil.convert(category, new CategoryDto());
        dto.setHasProduct(!category.getProducts().isEmpty());
        return dto;
    }

    @Override
    public List<CategoryDto> listCategoryByCompany() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return categoryRepository.findByCompanyIdOrderByDescriptionAsc(companyId).stream()
                .map(category -> {
                    CategoryDto categoryDto = mapperUtil.convert(category, new CategoryDto());
                    List<ProductDto> product = productService.findAllByCategory(category);
                    categoryDto.setHasProduct(!product.isEmpty());
                    return categoryDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void save(CategoryDto dto) {
        CompanyDto currentUsersCompany = companyService.getCompanyDtoByLoggedInUser();
        dto.setCompany(currentUsersCompany);
        categoryRepository.save(mapperUtil.convert(dto, new Category()));
    }

    @Override
    public void update(CategoryDto dto) {
        CompanyDto currentUsersCompany = companyService.getCompanyDtoByLoggedInUser();
        dto.setCompany(currentUsersCompany);
        categoryRepository.save(mapperUtil.convert(dto, new Category()));
    }

    @Override
    public boolean isDescriptionUnique(Long id, String description, Long excludeCategoryId) {
        List<Category> categories = categoryRepository.findByCompanyIdOrderByDescriptionAsc(id);
        return categories.stream()
                .filter(category -> !category.getId().equals(excludeCategoryId))
                .noneMatch(category -> category.getDescription().trim().equalsIgnoreCase(description.trim()));
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

}
