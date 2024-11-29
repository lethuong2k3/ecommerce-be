package net.fpoly.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fpoly.ecommerce.model.Token;
import net.fpoly.ecommerce.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    private TokenRepository tokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null && !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        Token storedToken = tokenRepo.findByAccessToken(token).orElse(null);
        if (token != null) {
            storedToken.setLoggedOut(true);
            tokenRepo.save(storedToken);
        }
    }
}
