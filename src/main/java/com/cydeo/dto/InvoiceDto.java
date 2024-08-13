package com.cydeo.dto;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String invoiceNo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InvoiceStatus invoiceStatus = InvoiceStatus.AWAITING_APPROVAL;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InvoiceType invoiceType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CompanyDto company;

    private ClientVendorDto clientVendor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal tax;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal total;


}
