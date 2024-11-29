package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.request.ProductRequest;
import org.springframework.data.domain.Page;


public interface ProductService {
    Product create(ProductRequest productRequest);
    Page<Product> getAllProducts(int sortType, int page, int limit);
}
