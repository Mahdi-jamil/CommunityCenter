package com.devesta.blogify.community.domain.mapper;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.community.domain.dto.FullDetailCommunityDto;
import com.devesta.blogify.post.domain.dto.ListPostDto;
import com.devesta.blogify.tag.TagDto;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface FullDetailCommunityMapper {

    FullDetailCommunityMapper INSTANCE = Mappers.getMapper(FullDetailCommunityMapper.class);

    @Mapping(source = "posts" , target = "postDtoList") // todo check if it will run ??
    @Mapping(source = "communityId" , target = "id")
    FullDetailCommunityDto communityToCommunityDto(Community community);

    List<ListPostDto> postsToDtoList(List<Post> posts);

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(source = "postId" , target = "id")
    ListPostDto postToDto(Post post);

    TagDto tagToDto(Tag tag);

}
