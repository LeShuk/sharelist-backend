package ru.sharelist.sharelist.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
