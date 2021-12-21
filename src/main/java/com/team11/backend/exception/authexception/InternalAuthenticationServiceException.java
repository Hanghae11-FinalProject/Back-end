package com.team11.backend.exception.authexception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class InternalAuthenticationServiceException extends AuthenticationServiceException {
    public InternalAuthenticationServiceException(String msg) {
        super(msg);
    }

    public InternalAuthenticationServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
