package ru.sharelist.sharelist.model;

public record JwtResponseDto(String accessToken, String refreshToken, String type) {
    public JwtResponseDto(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer_");
    }
}
