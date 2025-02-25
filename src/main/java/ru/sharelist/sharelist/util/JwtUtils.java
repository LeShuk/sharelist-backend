package ru.sharelist.sharelist.util;

import io.jsonwebtoken.Claims;
import ru.sharelist.sharelist.model.JwtAuthentication;

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
}
