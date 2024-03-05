package com.devesta.blogify.exception.exceptions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CommunityNameExistException extends RuntimeException {
    public CommunityNameExistException(@NotEmpty @NotNull String s) {
        super(s);
    }
}
