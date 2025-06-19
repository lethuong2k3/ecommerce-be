package net.fpoly.ecommerce.controller;

import jakarta.validation.Valid;
import net.fpoly.ecommerce.model.Review;
import net.fpoly.ecommerce.model.request.ReviewRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.ReviewSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ReviewController {

    @Autowired
    private ReviewSerive reviewSerive;

    @GetMapping("/reviews/{productId}")
    public ResponseEntity<?> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewSerive.findByProductId(productId));
    }

    @GetMapping("/user/review/{productId}")
    public ResponseEntity<?> getReview(@PathVariable Long productId, Principal principal) {
        try {
            return ResponseEntity.ok(reviewSerive.findByProductIdAndUser(productId, principal));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", e.getMessage()));
        }
    }

    @PostMapping("/review/create")
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest reviewRequest, Principal principal) {
        return ResponseEntity.ok(reviewSerive.createReview(reviewRequest, principal));
    }
}
