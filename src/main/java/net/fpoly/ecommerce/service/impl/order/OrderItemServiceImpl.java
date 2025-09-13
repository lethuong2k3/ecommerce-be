package net.fpoly.ecommerce.service.impl.order;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.exception.InsufficientStockException;
import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.repository.OrderItemRepo;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.repository.ProductDetailRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepo orderItemRepo;

    private final OrderRepo orderRepo;

    private final UserRepo userRepo;


    private final ProductDetailRepo productDetailRepo;



    private BigDecimal totalAmount(Order order) {
        return order.getOrderItems().stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemRepo.delete(orderItem);
        Order order = orderItem.getOrders();
        order.setTotalAmount(totalAmount(order));
        orderRepo.save(order);
    }

    private Integer checkQuantity(Integer quantity, ProductDetail productDetail) {
        ProductDetail productDetail1 = productDetailRepo.findById(productDetail.getId()).orElseThrow(() -> new IllegalArgumentException("Product detail not found"));
        if (quantity > productDetail1.getAmount()) {
            throw new InsufficientStockException("Product name " + productDetail1.getProduct().getName() + "is out of stock");
        }
        return quantity;
    }

    @Override
    public OrderItem updateQuantity(Long orderItemId, int quantity) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItem.setQuantity(checkQuantity(quantity, orderItem.getProductDetail()));
        orderItem.setTotalPrice(orderItem.getItemPrice().multiply(BigDecimal.valueOf(quantity)));
        orderItemRepo.save(orderItem);
        Order order = orderItem.getOrders();
        order.setTotalAmount(totalAmount(order));
        orderRepo.save(order);
        return orderItem;
    }

    @Override
    public void deleteAllOrderItems(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        List<OrderItem> orderItems = orderItemRepo.findAllByUserAndOrderStatus(user, OrderStatus.CART);
        orderItemRepo.deleteAll(orderItems);
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.CART);
        order.setTotalAmount(BigDecimal.ZERO);
        orderRepo.save(order);
    }
}
