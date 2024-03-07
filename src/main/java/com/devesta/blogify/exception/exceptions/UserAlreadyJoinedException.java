package com.devesta.blogify.exception.exceptions;

public class UserAlreadyJoinedException extends RuntimeException {
    public UserAlreadyJoinedException(String userAlreadyInThisCommunity) {
        super(userAlreadyInThisCommunity);
    }
}
