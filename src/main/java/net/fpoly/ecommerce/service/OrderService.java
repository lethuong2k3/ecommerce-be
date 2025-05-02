package net.fpoly.ecommerce.service;

import com.mservice.shared.exception.MoMoException;
import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;

import java.security.Principal;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest, Principal principal);
    Object updateOrder(OrderRequest orderRequest, Principal principal) throws Exception;
    OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus);
    OrderResponse restoreStockForOrderItems(Principal principal);
}
