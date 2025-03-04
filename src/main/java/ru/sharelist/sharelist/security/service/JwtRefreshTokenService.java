package ru.sharelist.sharelist.security.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.config.JwtRefreshConfigurationProperties;
import ru.sharelist.sharelist.security.model.entity.JwtRefreshToken;
import ru.sharelist.sharelist.security.repository.JwtRefreshTokenRepository;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtRefreshTokenService {
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final JwtRefreshConfigurationProperties jwtRefreshConfigurationProperties;

    public void save(JwtRefreshToken jwtRefreshToken) {
        jwtRefreshTokenRepository.save(jwtRefreshToken);
    }

    @Nullable
    public JwtRefreshToken get(String login) {
        return jwtRefreshTokenRepository.getRefreshTokenByLogin(login)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Очищает просроченные токены из таблицы
     */
    @Scheduled(cron = "${jwt.refresh.scheduling.cleanup.cron}")
    public void cleanup() {
        Instant expirationThreshold = Instant.now().minusMillis(jwtRefreshConfigurationProperties.getExpiration());
        List<JwtRefreshToken> expiredTokens = jwtRefreshTokenRepository.findAllByCreatedAtBefore(expirationThreshold);
        if (!expiredTokens.isEmpty()) {
            jwtRefreshTokenRepository.deleteAll(expiredTokens);
            log.info("Deleted {} expired refresh tokens", expiredTokens.size());
        } else {
            log.info("No expired refresh tokens found for cleanup");
        }
    }
}
