package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.request.OrderItemRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @DeleteMapping("/user/cart/{orderId}")
    public ApiResponse<?> deleteCart(@PathVariable Long orderId) {
        orderItemService.deleteOrderItem(orderId);
        return ApiResponse.success("Delete cart successfully");
    }

    @PutMapping("/user/cart/{orderId}")
    public ApiResponse<?> updateQuantity(@PathVariable Long orderId, @RequestBody OrderItemRequest request) {
        return ApiResponse.success(orderItemService.updateQuantity(orderId, request.getQuantity()));
    }
}
