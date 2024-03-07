package com.devesta.blogify.comment.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "commentId" , target = "commentId")
    @Mapping(source = "post.postId" , target = "postId")
    @Mapping(source = "author.username" , target = "authorUsername")
    CommentDto toCommentDto(Comment comment);



}
