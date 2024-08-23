package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, CompanyService companyService, @Lazy CategoryService categoryService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.categoryService = categoryService;
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> listProductsSortedByCategoryAndName() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> sortedProducts = productRepository.findByCompanyIdOrderByCategoryDescriptionAndProductNameAsc(companyId);
        return sortedProducts.stream()
                .map(product -> mapperUtil.convert(product, new ProductDto()))
                .toList();
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        CategoryDto categoryDto = categoryService.findByDescription(productDto.getCategory().getDescription());
        productDto.setCategory(categoryDto);
        Product product = mapperUtil.convert(productDto, new Product());
        Product saved = productRepository.save(product);
        return mapperUtil.convert(saved, new ProductDto());
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        ProductDto foundProduct = findById(id);
        productDto.setId(foundProduct.getId());
        return save(productDto);
    }

    @Override
    public void delete(Long id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        foundProduct.setIsDeleted(true);
        foundProduct.setName(foundProduct.getName() + "-" + foundProduct.getId());
        productRepository.save(foundProduct);
    }

    @Override
    public ProductDto findByNameInCompany(String name) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        Product product = productRepository.findByNameAndCategoryCompanyId(name, companyId).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> findAllByCategoryAndCompany(Category category) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> productList = productRepository.retrieveAllByCategoryIdAndCompanyId(category.getId(), companyId);
        return productList.stream().map(product -> mapperUtil.convert(product, new ProductDto())).toList();
    }


}

