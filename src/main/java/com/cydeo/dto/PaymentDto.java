package com.cydeo.dto;

import com.cydeo.enums.Months;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    @JsonIgnore
    private Long id;
    private Integer year;
    private Months month;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private boolean isPaid;
    @JsonIgnore
    private String companyStripeId;
    @JsonIgnore
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CompanyDto company;
}
