package ru.sharelist.sharelist.security.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt.access")
@Component
public class JwtAccessConfigurationProperties {
    private String secret;
    private Integer expiration;

    public Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Date getExpirationDate() {
        return Date.from(Instant.now().plusMillis(expiration));
    }
}
