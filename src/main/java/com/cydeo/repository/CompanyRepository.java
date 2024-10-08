package com.cydeo.repository;

import com.cydeo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c WHERE c.id <> 1 ORDER BY c.companyStatus ASC , c.title ASC")
    List<Company> findAllExcludingRootCompanySortedByStatusAndTitle();

    Company findByTitle(String companyTitle);
}
