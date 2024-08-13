package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientVendorDto {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "Company Name is required field.")
    @Size(min = 2,max = 50,message = "Last Name must be between 2 and 50 characters long")
    private String clientVendorName;


    @Pattern(regexp = "^(" +
            "\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" //+1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", // +111 123 456 789
            message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @Pattern(regexp = "^https?://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+(/[a-zA-Z0-9%&=?/.\\-_~#]*)?$",
            message = "Website should have a valid format.")
    private String website;


    @NotNull(message = "Please select type.")
    private ClientVendorType clientVendorType;

    @Valid
    @NotNull
    private AddressDto address;

    //JsonBackReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CompanyDto company;

    private boolean hasInvoice;

}
