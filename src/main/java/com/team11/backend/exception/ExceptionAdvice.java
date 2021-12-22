package com.team11.backend.exception;

import com.team11.backend.exception.errorUtils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse validException(
            MethodArgumentNotValidException ex) {
        return ErrorResponse.builder()
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler({Exception.class})
    public ErrorResponse exception(
            Exception ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}

