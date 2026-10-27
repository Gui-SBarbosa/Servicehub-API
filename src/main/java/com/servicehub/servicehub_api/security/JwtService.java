package com.servicehub.servicehub_api.security;

import com.servicehub.servicehub_api.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationSeconds;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        if (secret.length() < 32) throw new IllegalArgumentException("JWT_SECRET deve ter pelo menos 32 caracteres");
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); expirationSeconds = expirationMinutes * 60;
    }
    public String generate(UserAccount user) { Instant now = Instant.now(); return Jwts.builder().subject(user.getEmail()).claim("role", user.getRole().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds))).signWith(key).compact(); }
    public String subject(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject(); }
    public long expirationSeconds() { return expirationSeconds; }
}
