package com.cydeo.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {

    private String clientSecret;
    private String chargeId;
    private String description;
}
