package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShipmentRequest {
    @NotBlank(message = "First name cannot be null")
    private String firstName;

    @NotBlank(message = "Last name cannot be null")
    private String lastName;

    private String companyName;

    @NotBlank(message = "Company name cannot be null")
    private String country;

    @NotBlank(message = "Address name cannot be null")
    private String address;

    @NotBlank(message = "City name cannot be null")
    private String city;

    @NotBlank(message = "State name cannot be null")
    private String state;

    @NotBlank(message = "Phone name cannot be null")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,11}$", message = "Phone number is not valid")
    private String phone;

    @NotBlank(message = "Zipcode name cannot be null")
    private String zipCode;

    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email is not valid")
    private String email;

    private String notes;
}
