package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {
    List<Brand> findAllByStatus(Integer status);
}
