package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
    List<ClientVendor>findByCompany_TitleOrderByClientVendorTypeAscClientVendorNameAsc(String companyTitle);
    List<ClientVendor> findAllByClientVendorTypeAndCompany_Title(ClientVendorType clientVendorType, String companyTitle);
    boolean existsByClientVendorName(String clientVendorName);
}
