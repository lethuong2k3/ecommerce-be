package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Review;
import net.fpoly.ecommerce.model.request.ReviewRequest;
import net.fpoly.ecommerce.model.response.ReviewResponse;

import java.security.Principal;
import java.util.List;

public interface ReviewSerive {
    List<ReviewResponse> findByProductId(Long productId);
    ReviewResponse findByProductIdAndUser(Long productId, Principal principal);
    Review createReview(ReviewRequest reviewRequest, Principal principal);

}
