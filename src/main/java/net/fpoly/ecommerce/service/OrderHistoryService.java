package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.OrderHistory;

import java.security.Principal;
import java.util.List;

public interface OrderHistoryService {
    List<OrderHistory> getOrderHistory(Long orderCode, Principal principal);
}
