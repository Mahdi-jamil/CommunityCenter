package com.devesta.blogify.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtParsingException extends AuthenticationException {
    public JwtParsingException(String msg) {
        super(msg);
    }
}
