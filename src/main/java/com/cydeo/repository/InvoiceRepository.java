package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByInvoiceTypeAndCompanyIdOrderByInvoiceNoDesc(InvoiceType type, Long id);

    List<Invoice> findByClientVendor(ClientVendor clientVendor);

    List<Invoice> findByInvoiceTypeAndInvoiceStatusAndCompanyTitleOrderByDateAsc(InvoiceType type, InvoiceStatus status, String title);


    List<Invoice> findTop3ByCompanyIdAndInvoiceStatusOrderByDateDesc(Long id, InvoiceStatus status);

    @Query("SELECT i FROM Invoice i WHERE i.company.id = ?1 AND i.invoiceStatus = 'APPROVED' AND i.invoiceType = 'SALES'")
    List<Invoice> findApprovedSalesInvoices(Long id);
}

