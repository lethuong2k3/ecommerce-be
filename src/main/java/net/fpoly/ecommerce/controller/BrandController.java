package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}
