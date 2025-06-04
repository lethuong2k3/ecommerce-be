package net.fpoly.ecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.constants.VerifyUser;
import net.fpoly.ecommerce.model.request.RefreshTokenRequest;
import net.fpoly.ecommerce.model.request.SignUpRequest;
import net.fpoly.ecommerce.model.response.ApiResponse;
import net.fpoly.ecommerce.model.response.AuthenticationResponse;
import net.fpoly.ecommerce.model.request.SignInRequest;
import net.fpoly.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/info/{userId}")
    public ResponseEntity<ApiResponse<?>> getInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getInfo(userId)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
            }
            return ResponseEntity.ok(ApiResponse.success(userService.register(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.success(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody SignInRequest request, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(ApiResponse.errorBindingResult(bindingResult));
            }
            return ResponseEntity.ok(ApiResponse.success(userService.verify(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.success(e.getMessage()));
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return userService.refreshToken(request, response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUser verifyUser) {
        try {
            userService.verifyUser(verifyUser);
            return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công "));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            userService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
