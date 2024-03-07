package com.devesta.blogify.post.domain;

import java.time.LocalDate;

public record ListPostDto(
        Long postId,
        long communityId,
        String communityName,
        String authorUsername,
        String title,
        String body,
        Integer votes,
        LocalDate lastUpdate) {
}
