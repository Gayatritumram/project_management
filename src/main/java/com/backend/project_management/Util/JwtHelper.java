package com.backend.project_management.Util;

import com.backend.project_management.UserPermission.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours


   private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";


    // Retrieve username from JWT token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getRoleFromToken(String token) {
        // Get the single role from the "role" claim (assumed that only one role is stored)
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))  // Ensure you use the correct secret key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userRole", String.class);  // Get the single role from the claim, assuming it's a single string
    }

    // Retrieve claims from token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(Date.from(Instant.now()));
    }

    // Generate token for user
    public String generateToken(UserDetails userDetails, UserRole role) {
        Map<String, Object> claims = new HashMap<>();

        // Get all roles (though you're only storing one here)
        claims.put("userRole", "ROLE_" + role.name());  // Store only a single role

        return doGenerateToken(claims, userDetails.getUsername());
//        Map<String, Object> claims = new HashMap<>();
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        claims.put("roles", roles); // Store all roles
//        claims.put("userRole", "ROLE_" + role.name());
//
//        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Create token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}

//authentication