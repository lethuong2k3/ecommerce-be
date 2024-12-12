package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/user/cart")
    public ResponseEntity<?> addToCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest, principal));
    }
}
