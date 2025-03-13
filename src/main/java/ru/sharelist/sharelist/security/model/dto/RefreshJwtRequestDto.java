package ru.sharelist.sharelist.security.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Запрос для обновления access токена
 */
public record RefreshJwtRequestDto(
        @NotBlank(message = "укажите refresh_token")
        @JsonProperty("refresh_token")
        String refreshToken
) {

}
