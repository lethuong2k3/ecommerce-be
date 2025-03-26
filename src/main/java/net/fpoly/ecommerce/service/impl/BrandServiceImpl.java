package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Brand;
import net.fpoly.ecommerce.repository.BrandRepo;
import net.fpoly.ecommerce.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepo brandRepo;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepo.findAllByStatus(1);
    }
}
