package com.devesta.blogify.post.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PostDto(
        @NotNull @Min(value = 2)
        String title ,

        @NotNull @Min(value = 10)
        String body) {
}
