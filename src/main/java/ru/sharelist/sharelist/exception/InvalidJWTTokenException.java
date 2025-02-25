package ru.sharelist.sharelist.exception;

public class InvalidJWTTokenException extends Exception {

    private static final String MESSAGE = "Invalid JWT token";

    public InvalidJWTTokenException() {
        super(MESSAGE);
    }
}
