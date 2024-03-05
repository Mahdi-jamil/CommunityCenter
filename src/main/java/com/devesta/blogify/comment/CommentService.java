package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.comment.domain.CommentBody;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.exception.exceptions.CommentNotFoundException;
import com.devesta.blogify.exception.exceptions.PostNotFoundException;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;

    public List<CommentDto> getAllComments(Long postId) {
        return commentRepository.findAllByPost_PostId(postId)
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


    public CommentDto addNewComment(Long pid, CommentBody body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Comment comment = Comment.builder()
                .body(body.body())
                .author(user)
                .parentComment(null)
                .post(postRepository.findById(pid)
                        .orElseThrow(() -> new PostNotFoundException("post not found to comment")))
                .build();

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public CommentDto replyOnComment(Long pid, CommentBody body, Authentication authentication, Long commentId) {
        User user = (User) authentication.getPrincipal();

        Comment comment = Comment.builder()
                .body(body.body())
                .author(user)
                .parentComment(commentRepository.findById(commentId)
                        .orElseThrow(() -> new CommentNotFoundException("comment not found to reply")))
                .post(postRepository.findById(pid)
                        .orElseThrow(() -> new PostNotFoundException("post not found to comment")))
                .build();

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public List<CommentDto> getAllRepliesToComment(Long pid, Long commentId) {
        return null;
    }

    public CommentDto getCommentById(Long pid, Long commentId) {
        return null;
    }

    public void deleteCommentById(Long pid, Long commentId, Authentication authentication) {
    }

    public CommentDto updateCommentById(Long pid, Long commentId, CommentBody body, Authentication authentication) {
        return null;
    }
}
