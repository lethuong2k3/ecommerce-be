package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Payment;
import net.fpoly.ecommerce.model.PaymentType;
import net.fpoly.ecommerce.model.Users;

import java.math.BigDecimal;

public interface PaymentService {
    Payment createPayment(PaymentType payment, Users user, BigDecimal amount);
}
