package net.fpoly.ecommerce.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fpoly.ecommerce.model.Role;
import net.fpoly.ecommerce.model.Token;
import net.fpoly.ecommerce.model.request.SignUpRequest;
import net.fpoly.ecommerce.model.response.AuthenticationResponse;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.SignInRequest;
import net.fpoly.ecommerce.repository.TokenRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.JWTService;
import net.fpoly.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private AuthenticationManager authenManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private TokenRepo tokenRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public AuthenticationResponse register(SignUpRequest request) {
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user = repo.save(user);
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        saveUserToken(accessToken, refreshToken, user);
        return getAuthResponse(accessToken, refreshToken, user);
    }

    private AuthenticationResponse getAuthResponse(String accessToken, String refreshToken,Users user) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setId(user.getId());
        authResponse.setToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }

    private void saveUserToken(String accessToken, String refreshToken,Users user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepo.save(token);
    }

    @Override
    public AuthenticationResponse verify(SignInRequest request) {
        Authentication auth =
                authenManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
        Users user = repo.findByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);
        return getAuthResponse(accessToken, refreshToken, user);
    }

    private void revokeAllTokenByUser(Users user) {
        List<Token> validTokenListByUser = tokenRepo.findAllAccessTokenByUser(user.getId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t -> t.setLoggedOut(true));
        }
        tokenRepo.saveAll(validTokenListByUser);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Users user = repo.findByEmail(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (jwtService.validateRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());
            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);
            AuthenticationResponse authResponse = getAuthResponse(accessToken, refreshToken, user);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Users getInfo(Long id) {
        return repo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy userId " + id));
    }


}
