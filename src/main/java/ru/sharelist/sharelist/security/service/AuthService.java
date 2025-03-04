package ru.sharelist.sharelist.security.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.config.JwtAccessConfigurationProperties;
import ru.sharelist.sharelist.security.config.JwtRefreshConfigurationProperties;
import ru.sharelist.sharelist.security.exception.CustomBadCredentialsException;
import ru.sharelist.sharelist.security.exception.InvalidJWTTokenException;
import ru.sharelist.sharelist.security.model.dto.RefreshJwtRequestDto;
import ru.sharelist.sharelist.security.model.entity.Credentials;
import ru.sharelist.sharelist.security.model.JwtAuthentication;
import ru.sharelist.sharelist.security.model.dto.JwtRequestDto;
import ru.sharelist.sharelist.security.model.dto.JwtResponseDto;
import ru.sharelist.sharelist.security.model.entity.JwtRefreshToken;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final JwtRefreshConfigurationProperties jwtRefreshConfigurationProperties;
    private final JwtAccessConfigurationProperties jwtAccessConfigurationProperties;

    public JwtResponseDto login(JwtRequestDto jwtRequestDto, HttpServletResponse response)
            throws CustomBadCredentialsException {
        Credentials credentials = credentialService.getByLoginAndPassword(
                jwtRequestDto.login(), jwtRequestDto.password()
        );

        String accessToken = jwtProvider.generateAccessToken(credentials);
        String refreshToken = jwtProvider.generateRefreshToken(credentials);

        saveRefreshToken(credentials.getLogin(), refreshToken);
        setRefreshToken(response, refreshToken);

        return createJwtResponse(accessToken);
    }

    public JwtResponseDto refresh(RefreshJwtRequestDto refreshJwtRequestDto, HttpServletResponse response)
            throws InvalidJWTTokenException, CustomBadCredentialsException {
        if (refreshJwtRequestDto == null) {
            return null;
        }

        String refreshToken = refreshJwtRequestDto.refreshToken();
        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new InvalidJWTTokenException();
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();
        String existingRefreshToken = retrieveRefreshToken(login);

        if (!Objects.equals(existingRefreshToken, refreshToken)) {
            throw new InvalidJWTTokenException();
        }

        Credentials credentials = credentialService.getByLogin(login);

        String accessToken = jwtProvider.generateAccessToken(credentials);
        String newRefreshToken = jwtProvider.generateRefreshToken(credentials);

        saveRefreshToken(credentials.getLogin(), newRefreshToken);
        setRefreshToken(response, refreshToken);

        return createJwtResponse(accessToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Сохраняет refreshToken в базе данных
     */
    private void saveRefreshToken(String login, String token) {
        JwtRefreshToken refreshToken = new JwtRefreshToken();
        refreshToken.setLogin(login);
        refreshToken.setToken(token);

        jwtRefreshTokenService.save(refreshToken);
    }

    /**
     * Извлекает refreshToken из базы данных
     */
    private String retrieveRefreshToken(String login) {
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenService.get(login);
        return jwtRefreshToken == null ? null : jwtRefreshToken.getToken();
    }

    /**
     * Устанавливает refreshToken в cookie
     */
    private void setRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie newCookieToken = new Cookie(
                "refreshToken",
                jwtAccessConfigurationProperties.getPrefix() + " " + refreshToken
        );
        newCookieToken.setMaxAge(jwtRefreshConfigurationProperties.getExpiration() / 1000);

        response.addCookie(newCookieToken);
    }

    private JwtResponseDto createJwtResponse(String accessToken) {
        String value = jwtAccessConfigurationProperties.getPrefix() + " " + accessToken;
        return new JwtResponseDto(value);
    }
}
