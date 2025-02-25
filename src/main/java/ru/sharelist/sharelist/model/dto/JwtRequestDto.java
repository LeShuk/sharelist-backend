package ru.sharelist.sharelist.model.dto;

/**
 * Запрос для получения JWT токена
 */
public record JwtRequestDto(String login, String password) {

}
