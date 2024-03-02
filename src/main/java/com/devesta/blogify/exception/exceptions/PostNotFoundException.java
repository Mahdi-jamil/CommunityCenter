package com.devesta.blogify.exception.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String msg) {
        super(msg);
    }
}
