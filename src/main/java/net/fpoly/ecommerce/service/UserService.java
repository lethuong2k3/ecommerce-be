package net.fpoly.ecommerce.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fpoly.ecommerce.model.request.RefreshTokenRequest;
import net.fpoly.ecommerce.model.request.SignUpRequest;
import net.fpoly.ecommerce.model.response.AuthenticationResponse;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.SignInRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    AuthenticationResponse register(SignUpRequest request);
    AuthenticationResponse verify(SignInRequest request);
    ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
    Users getInfo(Long id);

}
