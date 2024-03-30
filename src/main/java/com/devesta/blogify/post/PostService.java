package com.devesta.blogify.post;

import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.exception.exceptions.notfound.PostNotFoundException;
import com.devesta.blogify.exception.exceptions.UnauthorizedAccessException;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.post.domain.PostDto;
import com.devesta.blogify.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public ListPostDto getPostDetail(Long id) {
        return postRepository.findPostDetail(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public Integer upVote(Long pid) {
        return postRepository.updateVotesByPostIdPlus(pid);
    }

    public Integer downVote(Long pid) {
        return postRepository.updateVotesByPostIdMinus(pid);
    }

    public PostDto updatePost(PostDto postDto, Long pid, Authentication authentication) {
        Post post = checkValidation(pid, authentication);

        post.setBody(postDto.body());
        post.setTitle(postDto.title());
        Post saved = postRepository.save(post);

        return new PostDto(saved.getTitle(), saved.getBody());
    }

    public void deletePost(Long pid, Authentication authentication) {
        checkValidation(pid, authentication);
        postRepository.deleteById(pid);
    }

    private Post checkValidation(Long pid, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Post post = postRepository.findById(pid)
                .orElseThrow(() -> new PostNotFoundException("post not found to update"));
        if (!post.getAuthor().equals(user))
            throw new UnauthorizedAccessException("User is not authorized to delete this post");

        return post;
    }

    public List<CommentDto> getPostComments(Long pid) {
        return postRepository.findCommentsByPostId(pid)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
