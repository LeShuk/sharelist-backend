package ru.sharelist.sharelist.security.service;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.sharelist.sharelist.security.config.JwtAccessConfigurationProperties;
import ru.sharelist.sharelist.security.config.JwtRefreshConfigurationProperties;
import ru.sharelist.sharelist.security.model.entity.Credentials;
import ru.sharelist.sharelist.security.util.JwtUtils;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(
        {JwtRefreshConfigurationProperties.class, JwtAccessConfigurationProperties.class}
)
public class JwtProvider {

    private final JwtAccessConfigurationProperties jwtAccessConfigurationProperties;
    private final JwtRefreshConfigurationProperties jwtRefreshConfigurationProperties;

    @Nullable
    public String generateAccessToken(Credentials credentials) {
        return JwtUtils.generateToken(
                credentials,
                jwtAccessConfigurationProperties.getExpirationDate(),
                jwtAccessConfigurationProperties.getSecretKey()
        );
    }

    @Nullable
    public String generateRefreshToken(Credentials credentials) {
        return JwtUtils.generateToken(
                credentials,
                jwtRefreshConfigurationProperties.getExpirationDate(),
                jwtRefreshConfigurationProperties.getSecretKey()
        );
    }

    public boolean isValidAccessToken(String accessToken) {
        return JwtUtils.isValidToken(accessToken, jwtAccessConfigurationProperties.getSecretKey());
    }

    public boolean isValidRefreshToken(String refreshToken) {
        return JwtUtils.isValidToken(refreshToken, jwtRefreshConfigurationProperties.getSecretKey());
    }

    public Claims getAccessClaims(String token) {
        return JwtUtils.getClaims(token, jwtAccessConfigurationProperties.getSecretKey());
    }

    public Claims getRefreshClaims(String token) {
        return JwtUtils.getClaims(token, jwtRefreshConfigurationProperties.getSecretKey());
    }

    public String getAccessPrefix() {
        return jwtAccessConfigurationProperties.getPrefix();
    }

    public String getRefreshPrefix() {
        return jwtRefreshConfigurationProperties.getPrefix();
    }

    public Integer getAccessExpiration() {
        return jwtAccessConfigurationProperties.getExpiration();
    }

    public Integer getRefreshExpiration() {
        return jwtRefreshConfigurationProperties.getExpiration();
    }
}
