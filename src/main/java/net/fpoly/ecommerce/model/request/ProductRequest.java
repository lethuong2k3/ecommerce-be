package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import net.fpoly.ecommerce.model.*;

import java.util.Date;
import java.util.Set;

@Data
public class ProductRequest {
    @NotBlank(message = "Name cannot be null")
    private String name;

    private String description;

    private Category category;

    private Brand brand;

    private Set<ProductMaterial> productMaterials;

    private Set<ProductDetail> productDetails;

    private Set<Image> images;
}
