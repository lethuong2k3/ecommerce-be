package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Category;
import net.fpoly.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN p.category c WHERE (:categoryName IS NULL OR c.name = :categoryName) AND (:keyword IS NULL OR UPPER(p.name) LIKE concat('%', upper(:keyword), '%'))")
    Page<Product> findAll(@Param("categoryName") String categoryName, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productDetails pd JOIN p.category c WHERE (:categoryName IS NULL OR c.name = :categoryName) AND (:keyword IS NULL OR UPPER(p.name) LIKE concat('%', upper(:keyword), '%')) ORDER BY pd.price ASC")
    Page<Product> findAllSortedByProductDetailPriceAsc(@Param("categoryName") String categoryName, @Param("keyword") String keyword,Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productDetails pd JOIN p.category c WHERE (:categoryName IS NULL OR c.name = :categoryName) AND (:keyword IS NULL OR UPPER(p.name) LIKE concat('%', upper(:keyword), '%')) ORDER BY pd.price DESC")
    Page<Product> findAllSortedByProductDetailPriceDesc(@Param("categoryName") String categoryName, @Param("keyword") String keyword,Pageable pageable);

    List<Product> findAllByCategoryAndIdNotAndStatus(Category category, Long id, Integer status,Pageable pageable);

}
