package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepo extends JpaRepository<ProductDetail, Long> {

}
