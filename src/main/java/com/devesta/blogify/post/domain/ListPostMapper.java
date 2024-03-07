package com.devesta.blogify.post.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ListPostMapper {

    List<ListPostDto> postsToDtoList(List<Post> posts);

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(source = "postId" , target = "postId")
    @Mapping(source = "community.communityId" , target = "communityId")
    @Mapping(source = "community.name" , target = "communityName")
    ListPostDto postToDto(Post post);
}

