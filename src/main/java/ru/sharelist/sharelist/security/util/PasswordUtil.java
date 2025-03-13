package ru.sharelist.sharelist.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    /**
     * Количество итераций при вычислении хэша пароля
     * <p>
     * Чем больше значение, тем больше вычислительных ресурсов потребуется для генерации хэша
     */
    private static final Integer STRENGTH = 16;

    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
