package net.fpoly.ecommerce.service.impl.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fpoly.ecommerce.constants.VerifyUser;
import net.fpoly.ecommerce.model.Role;
import net.fpoly.ecommerce.model.Token;
import net.fpoly.ecommerce.model.request.SignUpRequest;
import net.fpoly.ecommerce.model.response.AuthenticationResponse;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.SignInRequest;
import net.fpoly.ecommerce.repository.TokenRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.UserService;
import net.fpoly.ecommerce.service.impl.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public Users register(SignUpRequest request) {
        Users existingUser = repo.findByEmail(request.getEmail());
        if (existingUser != null) {
            if (existingUser.isEnabled()) {
                throw new RuntimeException("Email đã được sử dụng");
            } else {
                existingUser.setVerificationCode(generateVerificationCode());
                existingUser.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
                existingUser.setName(request.getName());
                existingUser.setPassword(encoder.encode(request.getPassword()));
                sendVerificationEmail(existingUser);
                return repo.save(existingUser);
            }
        }
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        user = repo.save(user);
        return user;
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
        try {
           authenManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Users user = repo.findByEmail(request.getEmail());
            if (!user.isEnabled()) {
                throw new RuntimeException("Tài khoản chưa được xác minh. ");
            }

            String accessToken = jwtService.generateAccessToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());
            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);
            return getAuthResponse(accessToken, refreshToken, user);

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Sai email hoặc mật khẩu");
        }
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
        if (user == null || !user.isEnabled()) {
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

    @Override
    public void verifyUser(VerifyUser input) {
        Users user = repo.findByEmail(input.getEmail());
        if (user == null) {
            throw new RuntimeException("Lỗi không tìm thấy user");
        }
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã xác minh đã hết hạn");
        }
        if (user.getVerificationCode().equals(input.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            repo.save(user);
        } else {
            throw new RuntimeException("Mã xác minh không đúng");
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        Users user = repo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Lỗi không tìm thấy user");
        }
        if (user.isEnabled()) {
            throw new RuntimeException("Tài khoản đã được xác minh.");
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        sendVerificationEmail(user);
        repo.save(user);
    }

    public void sendVerificationEmail(Users user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to Fpoly Clothes</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
