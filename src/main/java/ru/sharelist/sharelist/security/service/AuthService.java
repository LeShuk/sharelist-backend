package ru.sharelist.sharelist.security.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.exception.InvalidJWTTokenException;
import ru.sharelist.sharelist.security.model.dto.RefreshJwtRequestDto;
import ru.sharelist.sharelist.security.model.entity.Credentials;
import ru.sharelist.sharelist.security.model.JwtAuthentication;
import ru.sharelist.sharelist.security.model.dto.JwtRequestDto;
import ru.sharelist.sharelist.security.model.dto.JwtResponseDto;
import ru.sharelist.sharelist.security.model.entity.JwtRefreshToken;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    public JwtResponseDto login(JwtRequestDto jwtRequestDto, HttpServletResponse response) {
        Credentials credentials = credentialService.getByLoginAndPassword(
                jwtRequestDto.login(), jwtRequestDto.password()
        );

        String accessToken = jwtProvider.generateAccessToken(credentials);
        String refreshToken = jwtProvider.generateRefreshToken(credentials);

        updateRefreshToken(credentials.getLogin(), refreshToken);
        setRefreshTokenToCookie(response, refreshToken);

        return createJwtResponse(accessToken);
    }

    public JwtResponseDto refresh(RefreshJwtRequestDto refreshJwtRequestDto, HttpServletResponse response)
            throws InvalidJWTTokenException {
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

        updateRefreshToken(login, newRefreshToken);
        setRefreshTokenToCookie(response, refreshToken);

        return createJwtResponse(accessToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Удаляет из БД старые refreshToken пользователя и сохраняет новый
     */
    public void updateRefreshToken(String login, String token) {
        jwtRefreshTokenService.deleteAllByLogin(login);

        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
        jwtRefreshToken.setLogin(login);
        jwtRefreshToken.setToken(token);
        jwtRefreshToken.setExpiredAt(
                Instant.now().plusMillis(jwtProvider.getRefreshExpiration())
        );
        jwtRefreshTokenService.save(jwtRefreshToken);
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
    public void setRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie newCookieToken = new Cookie(
                "refreshToken",
                jwtProvider.getRefreshPrefix() + refreshToken
        );
        newCookieToken.setHttpOnly(true);
        newCookieToken.setMaxAge(jwtProvider.getRefreshExpiration() / 1000);

        response.addCookie(newCookieToken);
    }

    private JwtResponseDto createJwtResponse(String accessToken) {
        String value = jwtProvider.getAccessPrefix() + accessToken;
        return new JwtResponseDto(value);
    }
}
