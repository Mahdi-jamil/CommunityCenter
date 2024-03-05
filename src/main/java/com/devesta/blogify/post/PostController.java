package com.devesta.blogify.post;

import com.devesta.blogify.comment.CommentService;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.PostDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/{pid}")
    public ResponseEntity<ListPostDto> getDetailedPost(@PathVariable Long pid) {
        return new ResponseEntity<>(postService.getPostDetail(pid), HttpStatus.OK);
    }

    @PatchMapping("/up/{pid}")
    public ResponseEntity<Integer> upVote(@PathVariable Long pid) {
        return new ResponseEntity<>(postService.upVote(pid), HttpStatus.OK);
    }

    @PatchMapping("/down/{pid}")
    public ResponseEntity<Integer> downVote(@PathVariable Long pid) {
        return new ResponseEntity<>(postService.downVote(pid), HttpStatus.OK);
    }

    @PatchMapping("/{pid}")
    public ResponseEntity<PostDto> updatePost(
            @RequestBody @Valid PostDto postDto,
            @PathVariable Long pid,
            Authentication authentication) {
        return new ResponseEntity<>(postService.updatePost(postDto, pid, authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deletePost(@PathVariable Long pid,Authentication authentication){
        postService.deletePost(pid, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{pid}/comments")
    public ResponseEntity<List<CommentDto>> getAllPostComments(@PathVariable Long pid){
        return new ResponseEntity<>(postService.getPostComments(pid), HttpStatus.OK);
    }

}
