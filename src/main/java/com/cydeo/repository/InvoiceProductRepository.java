package com.cydeo.repository;

import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findAllByInvoiceId(Long id);

    @Query("SELECT DISTINCT ip.product FROM InvoiceProduct ip WHERE ip.invoice.id = ?1")
    List<Product> findProductsByInvoiceId(Long id);

    @Query("SELECT SUM(ip.quantity) FROM InvoiceProduct ip WHERE ip.invoice.id = ?1 AND ip.product.id = ?2")
    Integer sumQuantityOfProducts(Long id, Long productId);

    @Query("SELECT ip FROM InvoiceProduct ip WHERE ip.product.id = ?1 " +
            "AND ip.remainingQuantity > 0 AND ip.invoice.invoiceStatus = 'APPROVED' " +
            "AND ip.invoice.invoiceType = 'PURCHASE' AND ip.invoice.company.id = ?2 ORDER BY ip.id asc")
    List<InvoiceProduct> findApprovedPurchaseInvoices(Long productId, Long companyId);

    List<InvoiceProduct> findByInvoiceCompanyIdAndInvoiceInvoiceStatusOrderByInvoiceDateDesc(Long invoiceCompanyId, InvoiceStatus invoicInvoiceStatus);


}
