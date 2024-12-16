package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.OrderItemService;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/user/cart")
    public ResponseEntity<?> addToCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest, principal));
    }

    @GetMapping("/user/cart")
    public ResponseEntity<?> getCart(Principal principal) {
        return ResponseEntity.ok(orderService.findByUserAndOrderStatus(principal, OrderStatus.WAITING));
    }

    @DeleteMapping("/user/cart/{orderId}")
    public ApiResponse<?> deleteCart(@PathVariable Long orderId) {
        orderItemService.deleteOrderItem(orderId);
        return ApiResponse.success("Delete cart successfully");
    }
}
