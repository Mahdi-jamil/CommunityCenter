package com.devesta.blogify.user.domain.userlist;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDto(
        Long userId,
        String username,
        String image_url,
        LocalDate createdDate
) {
}