package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Review;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    Review findByProductIdAndUser(Long productId, Users user);
}
