package com.team11.backend.exception.authexception;

import org.springframework.http.HttpStatus;

public class JwtTokenException extends RuntimeException{

    private HttpStatus httpStatus;

    public JwtTokenException(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public JwtTokenException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtTokenException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public JwtTokenException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public JwtTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }

}
