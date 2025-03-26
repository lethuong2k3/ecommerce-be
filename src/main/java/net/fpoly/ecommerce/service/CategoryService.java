package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Category;
import net.fpoly.ecommerce.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAllByStatusAndCountProducts();
}
