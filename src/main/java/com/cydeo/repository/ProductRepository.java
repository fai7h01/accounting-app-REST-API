package com.cydeo.repository;

import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //product entity doesnt include a "company" field. bcs of that we need to filter products by using native query
    @Query("SELECT p FROM Product p WHERE p.category.company.id = :companyId ORDER BY p.category.description ASC, p.name ASC")
    List<Product> findByCompanyIdOrderByCategoryDescriptionAndProductNameAsc(@Param("companyId") Long companyId);

    @Query("SELECT p FROM Product p where p.category.id = ?1 AND p.category.company.id = ?2")
    List<Product> retrieveAllByCategoryIdAndCompanyId(Long categoryId, Long companyId);


}
