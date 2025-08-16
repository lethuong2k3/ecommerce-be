package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.request.OrderTrackingRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.security.Principal;


public interface OrderService {
    Order createOrder(OrderRequest orderRequest, Principal principal);
    Object updateOrder(OrderRequest orderRequest, Principal principal) throws Exception;
    OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus);
    Order findById(Long id);
    Page<OrderResponse> findByKeywordAndBetweenDate(OrderTrackingRequest request, Principal principal);
    OrderResponse restoreStockForOrderItems(Principal principal);
}
