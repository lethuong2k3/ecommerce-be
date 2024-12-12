package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.ProductDetail;
import net.fpoly.ecommerce.repository.ProductDetailRepo;
import net.fpoly.ecommerce.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    private ProductDetailRepo repo;

    @Override
    public ProductDetail getProductDetailById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
