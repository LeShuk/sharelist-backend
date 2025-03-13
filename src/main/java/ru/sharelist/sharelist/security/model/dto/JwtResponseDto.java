package ru.sharelist.sharelist.security.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ответ, возвращаемый пользователю, который содержит access и refresh токены
 */
public record JwtResponseDto(
        @JsonProperty("access_token")
        String accessToken) {
}
