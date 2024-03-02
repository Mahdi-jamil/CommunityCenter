package com.devesta.blogify.comment.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Long id,
        String authorUsername,
        String body,
        LocalDateTime lastUpdate,
        Integer votes,
        CommentDto parentComment) {
}
