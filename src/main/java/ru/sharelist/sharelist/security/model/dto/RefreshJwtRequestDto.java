package ru.sharelist.sharelist.security.model.dto;

/**
 * Запрос для обновления access токена
 */
public record RefreshJwtRequestDto(String refreshToken) {

}
