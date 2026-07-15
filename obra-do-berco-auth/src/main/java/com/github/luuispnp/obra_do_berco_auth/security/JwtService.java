package com.github.luuispnp.obra_do_berco_auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    private final SecretKey key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration}") long refreshExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String gerarAccessToken(UUID voluntarioId, String email, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + accessExpirationMs);
        return Jwts.builder()
                .subject(voluntarioId.toString())
                .claim("email", email)
                .claim("role", role)
                .claim("typ", "access")
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(key)
                .compact();
    }

    public RefreshTokenGerado gerarRefreshToken(UUID voluntarioId) {
        UUID jti = UUID.randomUUID();
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + refreshExpirationMs);
        String token = Jwts.builder()
                .subject(voluntarioId.toString())
                .id(jti.toString())
                .claim("typ", "refresh")
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(key)
                .compact();
        LocalDateTime expiraEm = LocalDateTime.ofInstant(expiracao.toInstant(), ZoneId.systemDefault());
        return new RefreshTokenGerado(token, jti, expiraEm);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getAccessExpirationSeconds() {
        return accessExpirationMs / 1000;
    }

    public record RefreshTokenGerado(String token, UUID jti, LocalDateTime expiraEm) {
    }

}
