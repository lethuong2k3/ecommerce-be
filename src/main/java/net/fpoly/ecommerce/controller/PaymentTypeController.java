package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @GetMapping("/payment-types")
    public ApiResponse<?> getAllPaymentTypes() {
        return ApiResponse.success(paymentTypeService.getAllPaymentTypes());
    }
}
