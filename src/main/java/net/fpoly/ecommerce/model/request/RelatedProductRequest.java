package net.fpoly.ecommerce.model.request;

import lombok.Data;
import net.fpoly.ecommerce.model.Category;

@Data
public class RelatedProductRequest {
    private Category category;
    private int limit;
    private Long productId;
}
