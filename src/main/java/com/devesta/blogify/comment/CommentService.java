package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDto> getAllComments(Long postId) {
        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(commentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllChildComments(Long commentID) {
        return commentRepository.findByParentComment_CommentId(commentID)
                .stream()
                .map(commentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }



}
