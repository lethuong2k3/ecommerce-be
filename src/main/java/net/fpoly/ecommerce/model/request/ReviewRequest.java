package net.fpoly.ecommerce.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.fpoly.ecommerce.model.Product;

@Data
public class ReviewRequest {

    private Long id;

    @NotNull
    private Product product;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @Size(max = 200, message = "Comment must be at most 200 characters")
    private String comment;
}
