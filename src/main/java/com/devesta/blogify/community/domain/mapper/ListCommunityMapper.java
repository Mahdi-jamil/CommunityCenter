package com.devesta.blogify.community.domain.mapper;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.tag.TagDto;
import com.devesta.blogify.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ListCommunityMapper {

    ListCommunityMapper INSTANCE = Mappers.getMapper(ListCommunityMapper.class);

    @Mapping(source = "communityId" , target = "id")
    @Mapping(source = "communityIconUrl" , target = "communityIconUrl")
    ListCommunityDto COMMUNITY_DTO(Community community);

    TagDto tagToDto(Tag tag);

}
