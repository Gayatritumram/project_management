package com.backend.project_management.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtHelper {

        @Value("${jwt.secret}")
        private String secretKey;

        @Value("${jwt.expiration}")
        private long expirationTime;

        private SecretKey getSigningKey() {
            // Use UTF-8 bytes of secret to build the HMAC SHA key
            return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }

        public String generateToken(String email) {
            // Generate JWT token with email as subject, issue time, expiration, and sign with secret
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(getSigningKey())  // default HS256
                    .compact();
        }

        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token);  // parse and validate signature & claims
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                // Log or handle expired, malformed, or unsupported JWT exceptions if needed
                return false;
            }
        }

        public String extractEmail(String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        }
    }


//authentication