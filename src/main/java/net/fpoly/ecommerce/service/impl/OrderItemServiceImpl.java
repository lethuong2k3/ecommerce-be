package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderItem;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.repository.OrderItemRepo;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

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

    @Override
    public void deleteAllOrderItems(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        List<OrderItem> orderItems = orderItemRepo.findAllByUserAndOrderStatus(user, OrderStatus.WAITING);
        orderItemRepo.deleteAll(orderItems);
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.WAITING);
        order.setTotalAmount(.0);
        orderRepo.save(order);
    }
}
