package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.request.OrderTrackingRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


public interface OrderService {
    Order createOrder(OrderRequest orderRequest, Principal principal);
    Object updateOrder(OrderRequest orderRequest, Principal principal) throws Exception;
    OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus);
    Page<OrderResponse> findByKeywordAndBetweenDate(OrderTrackingRequest request, Principal principal);
    Optional<Order> findByOrderCode(Long orderCode);
    void confirmPaymentByCode(Long orderCode) throws Exception;
    void cancelOrder(Order order, Principal principal) throws Exception;
    void expireWaitingOrders();
    OrderResponse orderDetails(Long orderCode, Principal principal);
}
