package com.devesta.blogify.community.domain.dto;

import com.devesta.blogify.tag.TagDto;

import java.util.List;

public record ListCommunityDto(
        Long id,
        String name,
        String description,
        Integer numberOfMembers,
        List<TagDto> tags
) {
}
