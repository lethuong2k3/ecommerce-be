package net.fpoly.ecommerce.repository;

import jakarta.persistence.LockModeType;
import net.fpoly.ecommerce.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepo extends JpaRepository<ProductDetail, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select pd from ProductDetail pd where pd.id = :id")
    Optional<ProductDetail> findByIdForUpdate(@Param("id") Long id);
}
