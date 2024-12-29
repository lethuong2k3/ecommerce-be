package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/product-detail/{id}")
    public ApiResponse<?> getProductDetailById(@PathVariable Long id) {
        return ApiResponse.success(productDetailService.getProductDetailById(id));
    }
}
