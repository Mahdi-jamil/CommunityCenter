package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.CommentBody;
import com.devesta.blogify.comment.domain.CommentDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentBody body,
            Authentication authentication) {
        return new ResponseEntity<>(commentService.addNewComment(postId, body, authentication), HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/{commentId}/replies")
    public ResponseEntity<CommentDto> addReplyComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentBody body,
            Authentication authentication,
            @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.replyOnComment(postId, body, authentication, commentId), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/{commentId}/replies")
    public ResponseEntity<List<CommentDto>> getAllRepliesToComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        List<CommentDto> replies = commentService.getAllRepliesToComment(postId, commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long commentId) {
        CommentDto comment = commentService.getCommentById(commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable Long commentId,
            Authentication authentication) {
        commentService.deleteCommentById(commentId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateCommentById(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentBody body,
            Authentication authentication) {
        CommentDto updatedComment = commentService.updateCommentById(commentId, body, authentication);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
}


