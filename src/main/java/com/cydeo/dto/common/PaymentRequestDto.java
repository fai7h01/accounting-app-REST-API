package com.cydeo.dto.common;

import com.cydeo.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long amount = 25000L;
    private Currency currency;

}
