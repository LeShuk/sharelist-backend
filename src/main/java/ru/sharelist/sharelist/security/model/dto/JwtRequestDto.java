package ru.sharelist.sharelist.security.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Запрос для получения JWT токена
 */
public record JwtRequestDto (
        @NotBlank(message = "укажите login")
        @Email(message = "login должен иметь формат email адресса")
        String login,

        @NotBlank(message = "укажите password")
        String password
) {

}
