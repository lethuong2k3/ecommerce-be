package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;

import java.security.Principal;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest, Principal principal);
    OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus);

}
