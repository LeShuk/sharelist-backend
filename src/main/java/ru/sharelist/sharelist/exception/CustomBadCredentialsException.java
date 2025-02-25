package ru.sharelist.sharelist.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class CustomBadCredentialsException extends BadCredentialsException {

    private static final String MESSAGE = "Bad credentials";

    public CustomBadCredentialsException() {
        super(MESSAGE);
    }
}
