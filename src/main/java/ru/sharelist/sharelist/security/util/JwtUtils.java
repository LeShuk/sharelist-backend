package ru.sharelist.sharelist.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import ru.sharelist.sharelist.security.model.JwtAuthentication;
import ru.sharelist.sharelist.security.model.entity.Credentials;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    /**
     * Используется для создания объекта {@link JwtAuthentication} из токена.
     * Объект {@link JwtAuthentication} содержит информацию об аутентификации пользователя, включая его имя.
     */
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setUsername(claims.getSubject());
        return jwtAuthentication;
    }

    public static boolean isValidToken(String token, Key secret) {
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

    public static Claims getClaims(String token, Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateToken(Credentials credentials, Date expirationDate, Key secret) {
        if (credentials == null) return null;

        return Jwts.builder()
                .subject(credentials.getLogin())
                .expiration(expirationDate)
                .signWith(secret)
                .compact();
    }
}
