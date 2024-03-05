package com.devesta.blogify.post.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ListPostMapper {
    ListPostMapper INSTANCE = Mappers.getMapper(ListPostMapper.class);

    List<ListPostDto> postsToDtoList(List<Post> posts);

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(source = "postId" , target = "id")
    ListPostDto postToDto(Post post);
}

