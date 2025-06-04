package net.fpoly.ecommerce.model.request;

import lombok.Data;
import net.fpoly.ecommerce.model.Product;

@Data
public class ReviewRequest {
    private Product product;
    private int rating;
    private String comment;
}
