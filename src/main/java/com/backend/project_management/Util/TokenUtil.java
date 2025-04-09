package com.backend.project_management.Util;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtil {

    private static final String SECRET_KEY = "798403f780912eea6f4efce9c5d382101adbd8884d51e3ef66d5e698d0c1791f"; // Use a secure key in production
    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds

    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}

