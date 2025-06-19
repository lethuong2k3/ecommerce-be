package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Review;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.ReviewRequest;
import net.fpoly.ecommerce.model.response.ReviewResponse;
import net.fpoly.ecommerce.repository.ReviewRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.ReviewSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewSerive {

    @Autowired
    private ReviewRepo repo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<ReviewResponse> findByProductId(Long productId) {
        return repo.findByProductId(productId).stream().map(ReviewResponse::convertToReviewResponse).collect(Collectors.toList());
    }

    @Override
    public ReviewResponse findByProductIdAndUser(Long productId, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Review review = repo.findByProductIdAndUser(productId, user);
        if (review == null) {
            throw new RuntimeException("Chưa có review");
        }
        return ReviewResponse.convertToReviewResponse(review);
    }

    @Override
    public Review createReview(ReviewRequest reviewRequest, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Review review = new Review();
        review.setProduct(reviewRequest.getProduct());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUser(user);
        review.setCreatedAt(new Date());
        return repo.save(review);
    }

}
