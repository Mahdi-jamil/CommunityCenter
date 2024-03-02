package com.devesta.blogify.user.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDto(
        Long userId,
        String username,
        String email,
        LocalDate createdDate,
        Role role) {
}