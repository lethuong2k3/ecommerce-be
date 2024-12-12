package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderExpirationService {
    @Autowired
    private OrderRepo orderRepo;
}
