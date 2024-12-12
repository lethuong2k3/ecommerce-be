package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.repository.OrderItemRepo;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.repository.ProductDetailRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private ProductDetailRepo productDetailRepo;

    private double totalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getItemPrice())
                .sum();
    }

    private void updateQuantityProductDT(Long productDetailId, int quantity) throws Exception {
        ProductDetail productDetail = productDetailRepo.findById(productDetailId).orElseThrow(() -> new Exception("Sản phẩm không tồn tại"));;
        if (productDetail.getAmount() < quantity) {
            throw new Exception("Không đủ số lượng trong kho");
        }
        productDetail.setAmount(productDetail.getAmount() - quantity);
        productDetailRepo.save(productDetail);
    }


    @Override
    public Order createOrder(OrderRequest orderRequest, Principal principal)  {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.PENDING);
        List<OrderItem> orderItems = orderRequest.getOrderItems();

        orderItems.forEach(item -> {
            item.setItemPrice(item.getProductDetail().getPrice());
            item.setQuantity(item.getQuantity());
        });
        try {
            updateQuantityProductDT(orderItems.get(0).getProductDetail().getId(), orderItems.get(0).getQuantity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (order == null) {
            Order newOrder = Order.builder()
                    .user(user)
                    .orderStatus(OrderStatus.PENDING)
                    .orderDate(new Date())
                    .orderItems(orderItems)
                    .totalAmount(totalAmount(orderItems))
                    .expirationTime(LocalDateTime.now().plusMinutes(5))
                    .build();
            orderItems.forEach(item -> item.setOrders(newOrder));
            return orderRepo.save(newOrder);
        }

        OrderItem existingItem = orderItemRepo.findByOrders_IdAndProductDetail_Id(order.getId(), orderItems.get(0).getProductDetail().getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + orderItems.get(0).getQuantity());
        } else {
            orderItems.forEach(item -> item.setOrders(order));
            order.getOrderItems().addAll(orderItems);
        }

        order.setTotalAmount(totalAmount(orderItems));
        return orderRepo.save(order);
    }

}
