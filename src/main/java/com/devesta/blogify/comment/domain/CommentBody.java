package com.devesta.blogify.comment.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentBody(@NotNull @NotBlank String body){}
