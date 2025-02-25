package ru.sharelist.sharelist.model.dto;

/**
 * Запрос для обновления access токена
 */
public record RefreshJwtRequestDto(String refreshToken) {

}
