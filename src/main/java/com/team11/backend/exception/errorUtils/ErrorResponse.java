package com.team11.backend.exception.errorUtils;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final LocalDateTime timestamp;
    private final int code;
    private final Object message;
}
