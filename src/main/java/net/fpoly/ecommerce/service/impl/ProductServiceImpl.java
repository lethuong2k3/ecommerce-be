package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo repo;

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
        Pageable pageable = PageRequest.of(page, limit == 0 ? 100 : limit, sort);
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

        List<ProductMaterial> productMaterials = productRequest.getProductMaterials().stream()
                .map(mt -> {
                    mt.setProduct(product);
                    return mt;
                })
                .collect(Collectors.toList());
        product.setProductMaterials(productMaterials);

        List<ProductDetail> productDetails = productRequest.getProductDetails().stream()
                .map(pd -> {
                    pd.setProduct(product);
                    return pd;
                })
                .collect(Collectors.toList());
        product.setProductDetails(productDetails);

        List<Image> images = productRequest.getImages().stream()
                .map(img -> {
                    img.setProduct(product);
                    return img;
                })
                .collect(Collectors.toList());
        product.setImages(images);

        return product;
    }

}
