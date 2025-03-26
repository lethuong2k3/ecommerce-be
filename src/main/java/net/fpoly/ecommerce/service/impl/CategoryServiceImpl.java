package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Category;
import net.fpoly.ecommerce.model.response.CategoryResponse;
import net.fpoly.ecommerce.repository.CategoryRepo;
import net.fpoly.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<CategoryResponse> findAllByStatusAndCountProducts() {
        return categoryRepo.findAllByStatusAndCountProducts();
    }
}
