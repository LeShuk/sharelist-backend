package ru.sharelist.sharelist.model.dto;

/**
 * Ответ, возвращаемый пользователю, который содержит access и refresh токены
 */
public record JwtResponseDto(String accessToken, String refreshToken, String type) {
    public JwtResponseDto(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer_");
    }
}
