package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.exception.InsufficientStockException;
import net.fpoly.ecommerce.model.request.OrderItemRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @DeleteMapping("/user/cart/{orderId}")
    public ApiResponse<?> deleteItemCart(@PathVariable Long orderId) {
        orderItemService.deleteOrderItem(orderId);
        return ApiResponse.success("Delete item cart successfully");
    }

    @PutMapping("/user/cart/{orderId}")
    public ApiResponse<?> updateQuantity(@PathVariable Long orderId, @RequestBody OrderItemRequest request) {
        try {
            return ApiResponse.success(orderItemService.updateQuantity(orderId, request.getQuantity()));
        } catch (InsufficientStockException e) {
            return ApiResponse.error("400", e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("500", e.getMessage());
        }
    }

    @DeleteMapping("/user/cart/delete")
    public ApiResponse<?> deleteAllItemCart(Principal principal) {
        orderItemService.deleteAllOrderItems(principal);
        return ApiResponse.success("Delete all item cart successfully");
    }
}
