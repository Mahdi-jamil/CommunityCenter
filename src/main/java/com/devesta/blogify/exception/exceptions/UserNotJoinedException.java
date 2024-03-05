package com.devesta.blogify.exception.exceptions;

public class UserNotJoinedException extends RuntimeException{
    public UserNotJoinedException(String msg){
        super(msg);
    }
}
