package com.backend.project_management.Util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkey"; // Use a strong secret key
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

     // Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

     //Extract Email from Token
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

      //Validate JWT Token
    public boolean validateToken(String token, String email) {
        try {
            return email.equals(extractEmail(token)) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

     //Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
