package com.devesta.blogify.user.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UpdatePayLoad(
        @Email
        @Nullable
        String email,
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,}$",
                message = "Password must be at least 6 characters long, contain at least one letter and one digit, and no whitespaces."
        )
        @Nullable
        String password) {
}
