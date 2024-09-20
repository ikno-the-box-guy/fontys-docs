package me.ikno.userapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, HashMap<String, Object> claims) {
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }
}
