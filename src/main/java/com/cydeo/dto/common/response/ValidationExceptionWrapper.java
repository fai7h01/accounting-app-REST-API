package com.cydeo.dto.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationExceptionWrapper {

    private String errorField;
    private Object rejectedValue;
    private String reason;

}
