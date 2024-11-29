package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotEmpty(message = "Token cannot be null")
    private String token;
}
