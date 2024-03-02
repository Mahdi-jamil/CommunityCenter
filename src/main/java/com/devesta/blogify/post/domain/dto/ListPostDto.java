package com.devesta.blogify.post.domain.dto;

import java.time.LocalDateTime;

public record ListPostDto(
        Long id,
        String authorUsername,
        String title,
        String body,
        Integer votes,
        LocalDateTime lastUpdate) {
}
