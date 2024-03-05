package com.devesta.blogify.community.domain.dto;

import com.devesta.blogify.tag.TagDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CommunityDto(
        @NotEmpty
        @NotNull
        String name,
        String description,
        List<TagDto> tags
) {
}
