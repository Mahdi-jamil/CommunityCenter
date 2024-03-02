package com.devesta.blogify.post;

import com.devesta.blogify.post.domain.dto.PostDetailDto;
import com.devesta.blogify.exception.exceptions.PostNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostDetailDto getPostDetail(Long id){
        return postRepository.findPostDetail(id)
                .orElseThrow(()-> new PostNotFoundException("Post not found"));
    }

}
