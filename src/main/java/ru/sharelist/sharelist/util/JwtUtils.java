package ru.sharelist.sharelist.util;

import io.jsonwebtoken.Claims;
import ru.sharelist.sharelist.model.JwtAuthentication;

public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }
}
