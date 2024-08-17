package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "Description is a required field.")
    @Size(min = 2, max = 100, message = "Description must be between 2 and 100 characters long.")
    @Column(unique = true, nullable = false)
    private String description;

    private CompanyDto company;

    @JsonIgnore
    private boolean hasProduct;
}
