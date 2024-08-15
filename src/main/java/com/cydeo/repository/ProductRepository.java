package com.cydeo.repository;

import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.company.id = :companyId ORDER BY p.category.description ASC, p.name ASC")
    List<Product> findByCompanyIdOrderByCategoryDescriptionAndProductNameAsc(@Param("companyId") Long companyId);

    @Query("SELECT p FROM Product p where p.category.id = ?1 AND p.category.company.id = ?2")
    List<Product> retrieveAllByCategoryIdAndCompanyId(Long categoryId, Long companyId);

    Optional<Product> findByNameAndCategoryCompanyId(String name, Long id);

}
