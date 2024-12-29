package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    @Query("SELECT p FROM Product p JOIN p.productDetails pd ORDER BY pd.price ASC")
    Page<Product> findAllSortedByProductDetailPriceAsc(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productDetails pd ORDER BY pd.price DESC")
    Page<Product> findAllSortedByProductDetailPriceDesc(Pageable pageable);
}
