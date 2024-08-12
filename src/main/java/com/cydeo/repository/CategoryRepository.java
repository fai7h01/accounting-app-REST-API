package com.cydeo.repository;

import com.cydeo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCompanyIdOrderByDescriptionAsc(Long companyId);
    Optional<Category> findByDescriptionAndCompanyId(String desc, Long companyId);

}
