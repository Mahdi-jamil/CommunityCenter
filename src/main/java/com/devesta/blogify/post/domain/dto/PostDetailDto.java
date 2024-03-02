package com.devesta.blogify.post.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailDto(
        Long id,
        String authorUsername,
        String title,
        String body,
        Integer votes,
        LocalDateTime lastUpdate
) {
}
