package com.devesta.blogify.post;

import com.devesta.blogify.post.domain.dto.PostDetailDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/{pid}")
    public ResponseEntity<PostDetailDto> getDetailedPost(@PathVariable Long pid){
        return new ResponseEntity<>(postService.getPostDetail(pid), HttpStatus.OK);
    }



}
