package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.exception.InsufficientStockException;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.OrderItemService;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/user/cart")
    public ApiResponse<?> addToCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        try {
            return ApiResponse.success(orderService.createOrder(orderRequest, principal));
        } catch (InsufficientStockException e) {
            return ApiResponse.error("400", e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("500", e.getMessage());
        }
    }

    @GetMapping("/user/cart")
    public ResponseEntity<?> getCart(Principal principal) {
        return ResponseEntity.ok(orderService.findByUserAndOrderStatus(principal, OrderStatus.WAITING));
    }


}
