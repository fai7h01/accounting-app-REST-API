package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByInvoiceTypeAndCompany_TitleOrderByInvoiceNoDesc(InvoiceType invoiceType, String title);

    List<Invoice> findByClientVendor(ClientVendor clientVendor);

    List<Invoice> findByInvoiceTypeAndInvoiceStatusAndCompanyTitleOrderByDateAsc(InvoiceType type, InvoiceStatus status, String title);


    List<Invoice> findTop3ByInvoiceStatusAndCompany_TitleOrderByDateDesc(InvoiceStatus status, String title);

    @Query("SELECT i FROM Invoice i WHERE i.company.id = ?1 AND i.invoiceStatus = 'APPROVED' AND i.invoiceType = 'SALES'")
    List<Invoice> findApprovedSalesInvoices(Long id);
}

