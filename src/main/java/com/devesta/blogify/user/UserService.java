package com.devesta.blogify.user;

import com.devesta.blogify.comment.CommentRepository;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.UserNotFoundException;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.devesta.blogify.user.domain.User;
import com.devesta.blogify.user.domain.UserDto;
import com.devesta.blogify.user.domain.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PostRepository postRepository;
    private final ListPostMapper listPostMapper;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentMapper commentMapper;
    private final ListCommunityMapper listCommunityMapper;

    public List<UserDto> getAllUser() {
        return userRepository
                .findAll(PageRequest.ofSize(10))
                .stream()
                .map(userMapper::userToUserDao)
                .collect(Collectors.toList());
    }

    public UserDto getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(UserMapper.INSTANCE::userToUserDao)
                .orElseThrow(() ->
                        new UserNotFoundException("user with username: " + username + " not found"));
    }

    public List<ListPostDto> getUserPosts(Long uid, String property, String order) {
        Sort sort = order.equals("desc")
                ? Sort.by(Sort.Direction.DESC, property)
                : Sort.by(Sort.Direction.ASC, property);

        return postRepository
                .findByAuthor_userId(uid, sort)
                .stream()
                .map(listPostMapper::postToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getUserComments(Long uid, String property, String order) {
        Sort sort = order.equals("desc")
                ? Sort.by(Sort.Direction.DESC, property)
                : Sort.by(Sort.Direction.ASC, property);

        return commentRepository
                .findByAuthor_userId(uid, sort)
                .stream()
                .map(commentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto partialUpdate(UpdatePayLoad updatePayLoad, Long uid) {
        User existingUser = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found for update"));

        Optional.ofNullable(updatePayLoad.email())
                .ifPresent(existingUser::setEmail);

        Optional.ofNullable(updatePayLoad.password())
                .map(passwordEncoder::encode)
                .ifPresent(existingUser::setPassword);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.INSTANCE.userToUserDao(updatedUser);
    }

    public List<ListCommunityDto> getUserCommunities(Long uid) {
        return userRepository.userCommunities(uid)
                .stream()
                .map(listCommunityMapper::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }
}
