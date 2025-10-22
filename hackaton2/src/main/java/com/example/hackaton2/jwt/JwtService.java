package com.example.hackaton2.jwt;


import com.example.hackaton2.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generate(User user, long expiresInSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(
                        "role", "ROLE_" + user.getRole().name(),
                        "branch", user.getBranch() == null ? "" : user.getBranch()
                ))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .signWith(key(), Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token);
    }
}