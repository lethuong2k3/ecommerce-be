package net.fpoly.ecommerce.controller;

import jakarta.validation.Valid;
import net.fpoly.ecommerce.model.request.ProductRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ApiResponse<?> getAllProducts(
            @RequestParam(required = false) int sortType,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int limit) {
        return ApiResponse.success(productService.getAllProducts(sortType, page, limit));
    }


    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(ApiResponse.success(productService.create(productRequest)));
    }
}
