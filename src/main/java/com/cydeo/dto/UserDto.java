package com.cydeo.dto;

import lombok.*;

import javax.validation.constraints.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;


    @NotBlank(message = "Email is required field.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9-]+(.[A-Za-z0-9-]+)@[^-][A-Za-z0-9-]+(.[A-Za-z0-9-]+)(.[A-Za-z]{2,})$", message = "Invalid email format.")
    private String username;


    @NotBlank(message = "Password is required field.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{4,}$",
            message = "Password should be at least 4 characters long and needs to contain 1 capital letter, 1 small letter and 1 special character or number.")
    private String password;


    @NotBlank(message = "Confirm Password is required field.")
    private String confirmPassword;


    @NotBlank(message = "First Name is required field.")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters long.")
    private String firstname;

    @NotBlank(message = "Last Name is required field.")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters long.")
    private String lastname;


    @Pattern(regexp = "^\\+1 \\(\\d{3}\\) \\d{3}-\\d{4}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;


    @NotNull(message = "Please select a Role.")
    private RoleDto role;


    @NotNull(message = "Please select a Company.")
    private CompanyDto company;


    private boolean isOnlyAdmin;


    public String getCompanyName() {
        return company != null ? company.getTitle() : "";
    }

    public String getRoleDescription() {
        return role != null ? role.getDescription() : "";
    }

}
