package com.cydeo.service;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;

import java.util.List;


public interface ProductService {

    ProductDto findById(Long id);
    List<ProductDto> listProductsByCategoryAndName();
    void save(ProductDto productDto);
    void update(ProductDto productDto);
    void delete(Long id);
//    List<ProductDto> listAllProductsByCompanyId(Long id);
//    List<ProductDto> findAllInStock();
    List<ProductDto> findAllByCategoryAndCompany(Category category);
//    boolean isNameUnique(Long categoryId, String name, Long excludeProductId);

}
