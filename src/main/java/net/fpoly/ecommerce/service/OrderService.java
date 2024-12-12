package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.request.OrderRequest;

import java.security.Principal;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest, Principal principal);
}
