package com.duc.ecommerce.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtService {
    String secretKey = "this-is-a-secret-key-for-my-ecommerce-project-2026";

    SecretKey key = Keys.hmacShaKeyFor(
            secretKey.getBytes()
    );

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis()+60*60*1000))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                //.parseEncryptedClaims(token)
                .getPayload();
        return claims.getSubject();

    }
}
