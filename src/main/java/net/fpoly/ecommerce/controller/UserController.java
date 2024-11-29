package net.fpoly.ecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.model.request.RefreshTokenRequest;
import net.fpoly.ecommerce.model.request.SignUpRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.model.response.AuthenticationResponse;
import net.fpoly.ecommerce.model.request.SignInRequest;
import net.fpoly.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(ApiResponse.success(userService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody SignInRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
        }
        return ResponseEntity.ok(ApiResponse.success(userService.verify(request)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return userService.refreshToken(request, response);
    }

    @GetMapping("/user/info/{userId}")
    public ResponseEntity<ApiResponse<?>> getInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getInfo(userId)));
    }

}
