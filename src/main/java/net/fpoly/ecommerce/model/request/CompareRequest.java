package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.fpoly.ecommerce.model.Product;

@Data
public class CompareRequest {

    private Long id;

    @NotNull(message = "product is not null")
    Product product;

}
