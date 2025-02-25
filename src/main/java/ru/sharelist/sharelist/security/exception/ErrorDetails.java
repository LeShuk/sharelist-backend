package ru.sharelist.sharelist.security.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ErrorDetails(
        @JsonProperty("exception_name")
        String exceptionName,

        String message,

        String time
) {
}
