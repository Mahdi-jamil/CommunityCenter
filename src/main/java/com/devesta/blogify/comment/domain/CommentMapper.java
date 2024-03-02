package com.devesta.blogify.comment.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    public CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);


    @Mapping(source = "commentId" , target = "id")
    @Mapping(source = "author.username" , target = "authorUsername")
    CommentDto toCommentDto(Comment comment);



}
