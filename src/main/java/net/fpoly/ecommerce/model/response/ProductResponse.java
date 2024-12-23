package net.fpoly.ecommerce.model.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private long id;

    private String name;

    private String description;

    private int status;

    private Category category;

    private Brand brand;

    private List<ProductMaterial> productMaterials;

    private List<Image> images;

    private String sku;


    public static ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .status(product.getStatus())
                .category(product.getCategory())
                .brand(product.getBrand())
                .productMaterials(product.getProductMaterials())
                .images(product.getImages())
                .build();
    }
}
