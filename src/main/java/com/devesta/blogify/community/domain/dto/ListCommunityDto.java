package com.devesta.blogify.community.domain.dto;

import com.devesta.blogify.tag.TagDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ListCommunityDto(
        Long id,
        @NotEmpty String name,
        String description,
        Integer numberOfMembers,
        String communityIconUrl,
        List<TagDto> tags
) {
}
