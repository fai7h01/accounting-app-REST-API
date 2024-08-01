package com.cydeo.dto;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;

    private String invoiceNo;

    private InvoiceStatus invoiceStatus = InvoiceStatus.AWAITING_APPROVAL;

    private InvoiceType invoiceType;

    private LocalDateTime date;

    private CompanyDto company;


    private ClientVendorDto clientVendor;

    private BigDecimal price;

    private BigDecimal tax;

    private BigDecimal total;


}
