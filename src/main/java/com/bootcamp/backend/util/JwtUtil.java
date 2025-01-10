package com.bootcamp.backend.util;

import com.bootcamp.backend.dto.LoginRequest;
import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final String secretKey = "YourSecureSecretKeyqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm"; // Use a fixed, secure key

    @Autowired
    private UserRepository userRepository;

    public String generateToken(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username).get();
        if(!password.equals(user.getPassword())){
            throw new Exception("Passwords Do not match !!");
        }
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", user.getRole())  // Add roles as a claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", String.class);
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}