package ru.sharelist.sharelist.security.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.exception.CustomBadCredentialsException;
import ru.sharelist.sharelist.security.exception.InvalidJWTTokenException;
import ru.sharelist.sharelist.security.model.Credentials;
import ru.sharelist.sharelist.security.model.JwtAuthentication;
import ru.sharelist.sharelist.security.model.dto.JwtRequestDto;
import ru.sharelist.sharelist.security.model.dto.JwtResponseDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;

    private final Map<String, String> refreshStorage = new HashMap<>();

    public JwtResponseDto login(JwtRequestDto jwtRequestDto) throws CustomBadCredentialsException {
        Credentials credentials = credentialService.getByLoginAndPassword(
                jwtRequestDto.login(), jwtRequestDto.password()
        );

        String accessToken = jwtProvider.generateAccessToken(credentials);
        String refreshToken = jwtProvider.generateRefreshToken(credentials);

        refreshStorage.put(credentials.getLogin(), refreshToken);

        return new JwtResponseDto(accessToken, refreshToken);

    }

    public JwtResponseDto refresh(String refreshToken) throws InvalidJWTTokenException, CustomBadCredentialsException {
        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new InvalidJWTTokenException();
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();
        String saveRefreshToken = refreshStorage.get(login);

        if (!Objects.equals(saveRefreshToken, refreshToken)) {
            throw new InvalidJWTTokenException();
        }

        Credentials credentials = credentialService.getByLogin(login);

        String accessToken = jwtProvider.generateAccessToken(credentials);
        final String newRefreshToken = jwtProvider.generateRefreshToken(credentials);
        refreshStorage.put(credentials.getLogin(), newRefreshToken);
        return new JwtResponseDto(accessToken, newRefreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
