package ru.sharelist.sharelist.security.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt.refresh")
public class JwtRefreshConfigurationProperties {
    private String secret;
    private Integer expiration;
    private String prefix;

    public Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Date getExpirationDate() {
        return Date.from(Instant.now().plusMillis(expiration));
    }
}
