package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.PaymentType;
import net.fpoly.ecommerce.repository.PaymentTypeRepo;
import net.fpoly.ecommerce.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentTypeRepo repo;

    @Override
    public List<PaymentType> getAllPaymentTypes() {
        return repo.findAll();
    }
}
