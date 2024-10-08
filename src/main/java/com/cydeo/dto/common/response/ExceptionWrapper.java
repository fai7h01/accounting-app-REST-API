package com.cydeo.dto.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionWrapper {

    private LocalDateTime timestamp;
    private boolean success;
    private Integer code;
    private String message;
    private String path;

    private Integer errorCount;
    private List<ValidationExceptionWrapper> validationExceptionList;

}
