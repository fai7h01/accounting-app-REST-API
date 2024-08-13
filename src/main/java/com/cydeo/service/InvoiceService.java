package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto findById(Long id);
    void delete(Long id);
    List<InvoiceDto> listAllByType(InvoiceType invoiceType);
    InvoiceDto generateInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType);
//    void update(InvoiceDto invoiceDto);
//    List<InvoiceDto> listAllByClientVendor(ClientVendor clientVendor);
//
//    void approve(InvoiceDto invoiceDto, InvoiceType invoiceType);
//    List<InvoiceDto> listTop3Approved(InvoiceStatus invoiceStatus);
    void setPriceTaxAndTotal(InvoiceDto invoiceDto);
//
//    List<InvoiceDto> findByInvoiceTypeAndStatus(InvoiceType type, InvoiceStatus status);
//
    InvoiceDto printInvoice(Long id);
//    BigDecimal countTotal(InvoiceType invoiceType);
//    BigDecimal sumProfitLoss();

}
