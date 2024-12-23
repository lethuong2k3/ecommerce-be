package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.OrderItem;

public interface OrderItemService {
    void deleteOrderItem(Long orderItemId);

    OrderItem updateQuantity(Long orderItemId, int quantity);
}
