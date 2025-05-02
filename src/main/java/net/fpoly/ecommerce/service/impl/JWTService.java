package net.fpoly.ecommerce.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Autowired
    private TokenRepo tokenRepo;

    private String secretKey = "";
    public JWTService () {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String username, long expireTime) {
        Map<String, Objects> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, 24*60*60*1000);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, 7*24*60*60*1000);
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValidToken = tokenRepo.findByAccessToken(token).map(t -> !t.isLoggedOut()).orElse(false);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && isValidToken;
    }

    public boolean validateRefreshToken(String token, Users user) {
        final String username = extractUsername(token);
        boolean isValidRefreshToken = tokenRepo.findByRefreshToken(token).map(t -> !t.isLoggedOut()).orElse(false);

        return (username.equals(user.getEmail())) && !isTokenExpired(token) && isValidRefreshToken;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
