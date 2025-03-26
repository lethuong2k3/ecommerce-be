package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Category;
import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.request.ProductRequest;
import net.fpoly.ecommerce.model.request.RelatedProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProductService {
    Product create(ProductRequest productRequest);
    Page<Product> getAllProducts(String categoryName, String keyword,int sortType, int page, int limit);
    Product getProduct(Long id);
    List<Product> relatedProducts(RelatedProductRequest request);

}
