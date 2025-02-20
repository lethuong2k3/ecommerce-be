package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.fpoly.ecommerce.model.Product;

@Data
public class WishListRequest {
    private Long id;

    @NotNull
    Product product;

}
