package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.OrderItem;

import java.security.Principal;

public interface OrderItemService {
    void deleteOrderItem(Long orderItemId);

    OrderItem updateQuantity(Long orderItemId, int quantity);

    void deleteAllOrderItems(Principal principal);
}
