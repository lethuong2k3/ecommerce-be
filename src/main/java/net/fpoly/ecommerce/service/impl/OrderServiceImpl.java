package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.OrderItemResponse;
import net.fpoly.ecommerce.model.response.OrderResponse;
import net.fpoly.ecommerce.model.response.ProductDetailResponse;
import net.fpoly.ecommerce.model.response.ProductResponse;
import net.fpoly.ecommerce.repository.OrderItemRepo;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.repository.ProductDetailRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;


    private double totalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getItemPrice())
                .sum();
    }

    @Override
    public Order createOrder(OrderRequest orderRequest, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.WAITING);
        List<OrderItem> orderItems = orderRequest.getOrderItems();

        // Thiết lập giá và số lượng cho các order items
        orderItems.forEach(item -> {
            item.setItemPrice(item.getProductDetail().getPrice());
            item.setQuantity(item.getQuantity());
            // Đảm bảo rằng trường orders của OrderItem được thiết lập
            item.setOrders(order);
        });

        if (order == null) {
            Order newOrder = Order.builder()
                    .user(user)
                    .orderStatus(OrderStatus.WAITING)
                    .orderDate(new Date())
                    .orderItems(orderItems)
                    .totalAmount(totalAmount(orderItems))
                    .build();
            orderItems.forEach(item -> item.setOrders(newOrder));
            orderRepo.save(newOrder);
            return newOrder;
        }

        OrderItem existingItem = orderItemRepo.findByOrders_IdAndProductDetail_Id(order.getId(), orderItems.get(0).getProductDetail().getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + orderItems.get(0).getQuantity());
            order.getOrderItems().add(existingItem);
        } else {
            orderItems.get(0).setOrders(order);
            order.getOrderItems().add(orderItems.get(0));
        }

        // Tính toán lại tổng tiền và lưu đơn hàng
        order.setTotalAmount(totalAmount(order.getOrderItems()));
        return orderRepo.save(order);
    }




    @Override
    public OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus) {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, orderStatus);
        if (order == null) {
            return null;
        }
        return OrderResponse.convertToOrderResponse(order);
    }

}
