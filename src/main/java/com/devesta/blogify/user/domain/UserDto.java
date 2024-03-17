package com.devesta.blogify.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDto(
        Long userId,
        String username,
        String email,
        String bio,
        LocalDate createdDate,
        Role role
) {
}