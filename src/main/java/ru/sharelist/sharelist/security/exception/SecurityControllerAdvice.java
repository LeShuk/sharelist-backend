package ru.sharelist.sharelist.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ControllerAdvice
public class SecurityControllerAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) {
        return response(HttpStatus.FORBIDDEN, ex);
    }

    private ResponseEntity<ErrorDetails> response(HttpStatus status, Exception ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .time(Instant.now().toString())
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .build();
        return ResponseEntity.status(status).body(errorDetails);
    }
}
