package net.fpoly.ecommerce.controller;

import jakarta.validation.Valid;
import net.fpoly.ecommerce.model.request.CompareRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.service.CompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CompareController {
    @Autowired
    private CompareService compareService;

    @GetMapping("/user/list-compare")
    public ResponseEntity<ApiResponse<?>> listCompareByUser(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(compareService.findAllByUserAndStatus(principal)));
    }

    @PostMapping("/user/compare-product")
    public ResponseEntity<?> getCompareByProductAndUser(@Valid @RequestBody CompareRequest compareRequest, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(compareService.findByProductAndUser(compareRequest.getProduct(), principal));
    }

    @PostMapping("/user/create-compare")
    public ResponseEntity<ApiResponse<?>> createCompare(
            @Valid @RequestBody CompareRequest request,
            Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(ApiResponse.success(compareService.createCompare(request, principal)));
    }

    @DeleteMapping("/user/delete-compare/{id}")
    public ResponseEntity<?> deleteCompare(
            @PathVariable Long id, Principal principal) {
        compareService.deleteCompare(id, principal);
        return ResponseEntity.ok("Deleted compare successfully");
    }

    @PostMapping("/user/delete-all-compare")
    public ResponseEntity<ApiResponse<?>> deleteAllCompare(@Valid @RequestBody List<CompareRequest> requests, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        compareService.deleteAllCompare(requests, principal);
        return ResponseEntity.ok(ApiResponse.success("Deleted all products to compare successfully"));
    }

}
