package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.model.Payment;
import net.fpoly.ecommerce.model.PaymentType;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.repository.PaymentRepo;
import net.fpoly.ecommerce.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;

    @Override
    public Payment createPayment(PaymentType paymentType, Users user, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setUser(user);
        payment.setCreatedAt(new Date());
        payment.setTotalAmount(amount);
        return paymentRepo.save(payment);
    }
}
