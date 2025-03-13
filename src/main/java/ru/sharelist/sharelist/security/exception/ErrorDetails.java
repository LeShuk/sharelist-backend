package ru.sharelist.sharelist.security.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorDetails(
        @JsonProperty("exception_name")
        String exceptionName,

        String message,

        Instant time
) {
}
