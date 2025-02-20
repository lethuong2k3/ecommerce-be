package net.fpoly.ecommerce.controller;

import jakarta.validation.Valid;
import net.fpoly.ecommerce.model.WishList;
import net.fpoly.ecommerce.model.request.WishListRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class WishListController {
    
    @Autowired
    private WishListService wishListService;

    @GetMapping("/user/wishlist")
    public ResponseEntity<?> getWishList (Principal principal) {
        return ResponseEntity.ok(wishListService.findAllByUserAndStatus(principal));
    }

    @PostMapping("/user/create-wishlist")
    public ResponseEntity<ApiResponse<?>> createWishList (@Valid @RequestBody WishListRequest request, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(ApiResponse.success(wishListService.createWishList(request, principal)));
    }

    @DeleteMapping("/user/delete-wishlist/{id}")
    public ResponseEntity<?> deleteWishList (@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(wishListService.deleteWishList(id, principal));
    }

    @PostMapping("/user/delete-all-wishlist")
    public ResponseEntity<?> deleteAllWishLists(@RequestBody List<WishListRequest> requests, Principal principal) {
        wishListService.deleteAllWishLists(requests, principal);
        return ResponseEntity.ok("Remove all wish lists");
    }

}
