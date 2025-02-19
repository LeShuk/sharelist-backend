package ru.sharelist.sharelist.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.model.Credentials;
import ru.sharelist.sharelist.model.JwtAuthentication;
import ru.sharelist.sharelist.model.JwtRequestDto;
import ru.sharelist.sharelist.model.JwtResponseDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;

    private final Map<String, String> refreshStorage = new HashMap<>();

    public JwtResponseDto login(JwtRequestDto jwtRequestDto) throws Exception {
        Credentials credentials = credentialService.getByLogin(jwtRequestDto.login())
                .orElseThrow(() -> new Exception("Пользователь не найден"));

        if (!Objects.equals(credentials.password(), jwtRequestDto.password())) {
            throw new Exception("Неправильный пароль");
        }

        String accessToken = jwtProvider.generateAccessToken(credentials);
        String refreshToken = jwtProvider.generateRefreshToken(credentials);

        refreshStorage.put(credentials.login(), refreshToken);

        return new JwtResponseDto(accessToken, refreshToken);

    }

    public JwtResponseDto getAccessToken(String refreshToken) throws Exception {
        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            return new JwtResponseDto(null, null);
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();
        String saveRefreshToken = refreshStorage.get(login);

        if (!Objects.equals(saveRefreshToken, refreshToken)) {
            return new JwtResponseDto(null, null);
        }

        Credentials credentials = credentialService.getByLogin(login)
                .orElseThrow(() -> new Exception("Пользователь не найден"));
        String accessToken = jwtProvider.generateAccessToken(credentials);
        return new JwtResponseDto(accessToken, null);
    }

    public JwtResponseDto refresh(String refreshToken) throws Exception {
        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new Exception("Невалидный JWT токен");
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();
        String saveRefreshToken = refreshStorage.get(login);

        if (!Objects.equals(saveRefreshToken, refreshToken)) {
            throw new Exception("Невалидный JWT токен");
        }

        Credentials credentials = credentialService.getByLogin(login)
                .orElseThrow(() -> new Exception("Пользователь не найден"));

        String accessToken = jwtProvider.generateAccessToken(credentials);
        final String newRefreshToken = jwtProvider.generateRefreshToken(credentials);
        refreshStorage.put(credentials.login(), newRefreshToken);
        return new JwtResponseDto(accessToken, newRefreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
