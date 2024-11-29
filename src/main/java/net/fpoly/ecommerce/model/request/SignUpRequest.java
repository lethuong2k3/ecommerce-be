package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignUpRequest {
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
