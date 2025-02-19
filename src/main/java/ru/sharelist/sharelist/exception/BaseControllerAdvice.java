package ru.sharelist.sharelist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class BaseControllerAdvice {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) {
        return response(HttpStatus.FORBIDDEN, ex);
    }

    private ResponseEntity<ErrorDetails> response(HttpStatus status, Exception ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .time(LocalDateTime.now().format(dateTimeFormatter))
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .build();
        return ResponseEntity.status(status).body(errorDetails);
    }
}
