package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Category;
import net.fpoly.ecommerce.model.response.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("""
select new net.fpoly.ecommerce.model.response.CategoryResponse(c.id, c.name, c.imageUrl, c.status, COUNT(p)) from Category c join c.products p WHERE c.status = 1 GROUP BY c.id, c.name, c.imageUrl, c.status
""")
    List<CategoryResponse> findAllByStatusAndCountProducts();
}
