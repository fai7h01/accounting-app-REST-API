package com.cydeo.service.impl;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> listProductsByCategoryAndName() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> sortedProducts = productRepository.findByCompanyIdOrderByCategoryDescriptionAndProductNameAsc(companyId);
        return sortedProducts.stream()
                .map(product -> mapperUtil.convert(product, new ProductDto()))
                .toList();
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        //product.setCategory(mapperUtil.convert(productDto.getCategory(), new Category()));
        Product saved = productRepository.save(product);
        return mapperUtil.convert(saved, new ProductDto());
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Product deletedId = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        deletedId.setIsDeleted(true);
        productRepository.save(deletedId);
    }

    @Override
    public List<ProductDto> findAllByCategoryAndCompany(Category category) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> productList = productRepository.retrieveAllByCategoryIdAndCompanyId(category.getId(), companyId);
        return productList.stream().map(product -> mapperUtil.convert(product, new ProductDto())).collect(Collectors.toList());
    }


}

