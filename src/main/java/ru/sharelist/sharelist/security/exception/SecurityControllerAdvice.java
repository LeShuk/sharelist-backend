package ru.sharelist.sharelist.security.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sharelist.sharelist.security.controller.AuthController;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice(assignableTypes = {AuthController.class})
public class SecurityControllerAdvice {
    @ExceptionHandler({CustomBadCredentialsException.class, InvalidJWTTokenException.class})
    public ResponseEntity<ErrorDetails> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response(ex, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response(ex, message));
    }

    private ErrorDetails response(Exception ex, String message) {
        return ErrorDetails.builder()
                .time(Instant.now())
                .message(message)
                .exceptionName(ex.getClass().getSimpleName())
                .build();
    }
}
