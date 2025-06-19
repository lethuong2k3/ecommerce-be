package net.fpoly.ecommerce.model.response;

import lombok.Builder;
import lombok.Data;
import net.fpoly.ecommerce.model.Review;
import net.fpoly.ecommerce.model.Users;

import java.util.Date;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private Date createdAt;
    private String userName;

    public static ReviewResponse convertToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .userName(review.getUser().getName())
                .build();
    }
}
