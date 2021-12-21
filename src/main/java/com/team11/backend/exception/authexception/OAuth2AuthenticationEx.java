package com.team11.backend.exception.authexception;

import org.springframework.http.HttpStatus;

public class OAuth2AuthenticationEx extends RuntimeException {

    private HttpStatus httpStatus;

    public OAuth2AuthenticationEx(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public OAuth2AuthenticationEx(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public OAuth2AuthenticationEx(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public OAuth2AuthenticationEx(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public OAuth2AuthenticationEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }
}
