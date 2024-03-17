package com.devesta.blogify.search;

import com.devesta.blogify.comment.CommentRepository;
import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.community.CommunityRepository;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.UserDto;
import com.devesta.blogify.user.domain.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final ListPostMapper listPostMapper;
    private final CommunityRepository communityRepository;
    private final ListCommunityMapper listCommunityMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<ListPostDto> searchForPost(String title, LocalDate postedAfter, String property, String order) {
        List<Post> posts;
        if (postedAfter == null)
            postedAfter = LocalDate.of(1900, 1, 1);

        if (property == null) {
            posts = postRepository.findRandomByTitleContainsIgnoreCaseAndLastUpdateAfter(title, postedAfter);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(order), property);
            posts = postRepository.findByTitleContainsIgnoreCaseAndLastUpdateAfter(title, postedAfter, sort);
        }
        return posts
                .stream()
                .map(listPostMapper::postToDto)
                .collect(Collectors.toList());
    }

    public List<ListCommunityDto> searchForCommunities(String name) {
        return communityRepository.findByNameContainsIgnoreCase(name)
                .stream()
                .map(listCommunityMapper::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    public List<CommentDto> searchForComments(String body, String property) {
        List<Comment> comments;
        if (property != null)
            comments = commentRepository.findAllByBodyContainsIgnoreCase(body, Sort.by(property));
        else
            comments = commentRepository.findAllByBodyContainsIgnoreCase(body);

        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDto> searchForPeople(String username) {
        return userRepository.findAllByUsernameContainsIgnoreCase(username)
                .stream()
                .map(userMapper::userToUserDao)
                .collect(Collectors.toList());
    }
}
