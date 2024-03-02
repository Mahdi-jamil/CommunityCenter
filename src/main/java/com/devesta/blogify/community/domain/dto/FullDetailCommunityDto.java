package com.devesta.blogify.community.domain.dto;

import com.devesta.blogify.post.domain.dto.ListPostDto;
import com.devesta.blogify.tag.Tag;
import lombok.Builder;

import java.util.List;

@Builder
public record FullDetailCommunityDto(
        Long id,
        String name,
        String description,
        Integer numberOfMembers,
        List<ListPostDto> postDtoList,
        List<Tag> tags
) {}
