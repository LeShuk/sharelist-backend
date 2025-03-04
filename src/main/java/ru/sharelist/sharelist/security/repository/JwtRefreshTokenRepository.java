package ru.sharelist.sharelist.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sharelist.sharelist.security.model.entity.JwtRefreshToken;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, UUID> {
    List<JwtRefreshToken> getRefreshTokenByLogin(String login);

    List<JwtRefreshToken> findAllByCreatedAtBefore(Instant createdAtBefore);
}
