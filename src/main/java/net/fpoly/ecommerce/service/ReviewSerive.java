package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Review;

import java.util.List;

public interface ReviewSerive {
    List<Review> findByProductId(Long productId);
    Review createReview(ReviewSerive review);
}
