package net.fpoly.ecommerce.controller;

import jakarta.validation.Valid;
import net.fpoly.ecommerce.model.request.ProductRequest;
import net.fpoly.ecommerce.model.request.RelatedProductRequest;
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
            @RequestParam(required = false) int limit,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String keyword
            ) {
        return ApiResponse.success(productService.getAllProducts(categoryName, keyword, sortType, page, limit));
    }

    @GetMapping("/product/{id}")
    public ApiResponse<?> getProduct(@PathVariable Long id) {
        return ApiResponse.success(productService.getProduct(id));
    }

    @PostMapping("/product/related")
    public ApiResponse<?> getRelatedProducts(@RequestBody RelatedProductRequest request) {
        return ApiResponse.success(productService.relatedProducts(request));
    }


//    @PostMapping("/create-product")
//    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
//        }
//        return ResponseEntity.ok(ApiResponse.success(productService.create(productRequest)));
//    }
}
