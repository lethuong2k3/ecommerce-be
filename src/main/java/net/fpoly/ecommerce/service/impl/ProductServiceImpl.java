package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.model.request.ProductRequest;
import net.fpoly.ecommerce.repository.ProductRepo;
import net.fpoly.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo repo;

    @Override
    public Product create(ProductRequest productRequest) {
        Product product = setProduct(productRequest);
        return repo.save(product);
    }

    @Override
    public Page<Product> getAllProducts(int sortType, int page, int limit) {
        Sort sort;
        switch (sortType) {
            case 4:
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case 5:
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "id");
                break;
        }
        Pageable pageable = PageRequest.of(page, limit, sort);
        return repo.findAll(pageable);
    }

    private static Product setProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCreatedAt(new Date());
        product.setStatus(1);
        product.setCategory(productRequest.getCategory());
        product.setBrand(productRequest.getBrand());
        Set<ProductMaterial> productMaterials = productRequest.getProductMaterials().stream().peek(mt -> mt.setProduct(product)).collect(Collectors.toSet());
        Set<ProductDetail> productDetails = productRequest.getProductDetails().stream().peek(pd -> pd.setProduct(product)).collect(Collectors.toSet());
        Set<Image> images = productRequest.getImages().stream().peek(pd -> pd.setProduct(product)).collect(Collectors.toSet());
        return product;
    }
}
