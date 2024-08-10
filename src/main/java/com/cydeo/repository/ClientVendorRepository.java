package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
//    List<ClientVendor>findByCompany_TitleOrderByClientVendorTypeAscClientVendorNameAsc(String companyTitle);
//    List<ClientVendor> findAllByClientVendorTypeAndCompany_Title(ClientVendorType clientVendorType, String companyTitle);
//    boolean existsByClientVendorName(String clientVendorName);

    List<ClientVendor> findAllByCompany_Id(Long id);
    Optional<ClientVendor> findByClientVendorNameAndCompanyId(String name, Long companyId);
}
