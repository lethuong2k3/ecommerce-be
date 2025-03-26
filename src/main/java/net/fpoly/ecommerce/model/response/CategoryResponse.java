package net.fpoly.ecommerce.model.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private int status;
    private Long countProducts;

    public CategoryResponse(Long id, String name, String imageUrl, int status, Long countProducts) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.status = status;
        this.countProducts = countProducts;
    }
}
