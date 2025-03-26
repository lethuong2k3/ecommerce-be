package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ApiResponse<?> getCategories() {
        return ApiResponse.success(categoryService.findAllByStatusAndCountProducts());
    }
}
