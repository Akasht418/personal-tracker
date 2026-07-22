package com.akash.personalTracker.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // Helper method to convert the secret string into a cryptographic SecretKey
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Generate Token for a User
    public String generateToken(String email, Integer userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId) // Custom claim storing database ID
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // 2. Extract Email (Subject) from Token
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // 3. Extract User ID from Token
    public Integer getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Integer.class);
    }

    // 4. Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Catches ExpiredJwtException, MalformedJwtException, SignatureException, etc.
            System.err.println("Invalid JWT Token: " + e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}