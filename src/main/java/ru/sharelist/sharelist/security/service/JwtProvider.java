package ru.sharelist.sharelist.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.sharelist.sharelist.security.config.JwtAccessConfigurationProperties;
import ru.sharelist.sharelist.security.config.JwtRefreshConfigurationProperties;
import ru.sharelist.sharelist.security.model.entity.Credentials;

import java.security.Key;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(
        value = {JwtRefreshConfigurationProperties.class, JwtAccessConfigurationProperties.class}
)
public class JwtProvider {

    private final JwtAccessConfigurationProperties jwtAccessConfigurationProperties;
    private final JwtRefreshConfigurationProperties jwtRefreshConfigurationProperties;

    @Nullable
    public String generateAccessToken(Credentials credentials) {
        if (credentials == null) return null;

        return Jwts.builder()
                .subject(credentials.getLogin())
                .expiration(jwtAccessConfigurationProperties.getExpirationDate())
                .signWith(jwtAccessConfigurationProperties.getSecretKey())
                .compact();
    }

    @Nullable
    public String generateRefreshToken(Credentials credentials) {
        if (credentials == null) return null;

        return Jwts.builder()
                .subject(credentials.getLogin())
                .expiration(jwtRefreshConfigurationProperties.getExpirationDate())
                .signWith(jwtRefreshConfigurationProperties.getSecretKey())
                .compact();
    }

    public boolean isValidAccessToken(String accessToken) {
        return isValidToken(accessToken, jwtAccessConfigurationProperties.getSecretKey());
    }

    public boolean isValidRefreshToken(String refreshToken) {
        return isValidToken(refreshToken, jwtRefreshConfigurationProperties.getSecretKey());
    }

    private boolean isValidToken(String token, Key secret) {
        if (token == null || secret == null) return false;

        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessConfigurationProperties.getSecretKey());
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshConfigurationProperties.getSecretKey());
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
