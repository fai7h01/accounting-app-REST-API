package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> listAllByInvoiceId(Long id);

    InvoiceProductDto save(InvoiceProductDto invoiceProductDto);

    void delete(Long id);

    BigDecimal getInvoiceProductTotalWithTax(InvoiceProductDto invoiceProductDto);

    BigDecimal getInvoiceProductTotalWithoutTax(InvoiceProductDto invoiceProductDto);

    void updateQuantityInStockForSale(Long id);
    void updateQuantityInStockForPurchase(Long id);
    void updateRemainingQuantityUponApproval(Long id);
    void calculateProfitLoss(Long id);

    List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long invoiceId);

    List<InvoiceProductDto> listAllApprovedInvoiceProducts();
}
