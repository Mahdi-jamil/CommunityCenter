package com.devesta.blogify.exception.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("User with email: " + email + " already exists!");
    }
}
