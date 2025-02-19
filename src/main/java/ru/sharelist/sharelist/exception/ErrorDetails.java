package ru.sharelist.sharelist.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorDetails {
    @JsonProperty("exception_name")
    private String exceptionName;

    private String message;

    private String time;
}
