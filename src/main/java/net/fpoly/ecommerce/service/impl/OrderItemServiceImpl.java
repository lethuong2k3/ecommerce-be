package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderItem;
import net.fpoly.ecommerce.repository.OrderItemRepo;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderRepo orderRepo;

    private double totalAmount(Order order) {
        return order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getItemPrice())
                .sum();
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemRepo.delete(orderItem);
        Order order = orderItem.getOrders();
        order.setTotalAmount(totalAmount(order));
        orderRepo.save(order);
    }

    @Override
    public OrderItem updateQuantity(Long orderItemId, int quantity) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(orderItem.getItemPrice() * quantity);
        orderItemRepo.save(orderItem);
        Order order = orderItem.getOrders();
        order.setTotalAmount(totalAmount(order));
        orderRepo.save(order);
        return orderItem;
    }
}
