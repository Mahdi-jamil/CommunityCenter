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

    @PostMapping("/{pid}/comment")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long pid,
            @RequestBody @Valid CommentBody body,
            Authentication authentication) {
        return new ResponseEntity<>(commentService.addNewComment(pid, body, authentication), HttpStatus.CREATED);
    }

    @PostMapping("/{pid}/comment/{commentId}")
    public ResponseEntity<CommentDto> addReplyComment(
            @PathVariable Long pid,
            @RequestBody @Valid CommentBody body,
            Authentication authentication,
            @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.replyOnComment(pid, body, authentication, commentId), HttpStatus.CREATED);
    }

    @GetMapping("/{pid}/comment/{commentId}/replies")
    public ResponseEntity<List<CommentDto>> getAllRepliesToComment(
            @PathVariable Long pid,
            @PathVariable Long commentId) {
        List<CommentDto> replies = commentService.getAllRepliesToComment(pid, commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @GetMapping("/{pid}/comment/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long pid,
            @PathVariable Long commentId) {
        CommentDto comment = commentService.getCommentById(pid, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{pid}/comment/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable Long pid,
            @PathVariable Long commentId,
            Authentication authentication) {
        commentService.deleteCommentById(pid, commentId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{pid}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateCommentById(
            @PathVariable Long pid,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentBody body,
            Authentication authentication) {
        CommentDto updatedComment = commentService.updateCommentById(pid, commentId, body, authentication);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

}
