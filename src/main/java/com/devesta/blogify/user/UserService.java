package com.devesta.blogify.user;

import com.devesta.blogify.comment.CommentRepository;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.exception.exceptions.UserNotFoundException;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.post.domain.dto.ListPostDto;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.devesta.blogify.user.domain.User;
import com.devesta.blogify.user.domain.UserDto;
import com.devesta.blogify.user.domain.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final CommentMapper commentMapper;

    public List<UserDto> getAllUserV2() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userToUserDao)
                .collect(Collectors.toList());
    }

    public UserDto getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(UserMapper.INSTANCE::userToUserDao)
                .orElseThrow(() ->
                        new UsernameNotFoundException("user with username: " + username + " not found"));
    }


    public List<ListPostDto> getUserPosts(String username, String property, String order) {
        Sort sort = Sort.by(property);
        if (order.equals("desc")) sort.descending();

        return postRepository
                .findByAuthor_Username(username, sort)
                .stream()
                .map(listPostMapper::postToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getUserComments(String username, String property, String order) {
        Sort sort = Sort.by(property);
        if (order.equals("desc")) sort.descending();

        return commentRepository
                .findByAuthor_Username(username, sort)
                .stream()
                .map(commentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    public Long deleteUserFromCommunity(Long cid, Long uid) {
        if (!userRepository.existsById(uid)) {
            throw new UserNotFoundException("user not found to be deleted");
        }
        return userRepository.deleteUserFromCommunity(cid, uid);
    }

    public Long update(UserDto userDto, Long uid) {
        if (!userRepository.existsById(uid)) {
            throw new UserNotFoundException("user not found for update");
        }
        return userRepository.save(userMapper.userDaoToUser(userDto)).getUserId();
    }

    public UserDto partialUpdate(UpdatePayLoad updatePayLoad, Long uid) {
        return userRepository.findById(uid).map(
                        existing -> {
                            if (Optional.ofNullable(updatePayLoad.email()).isPresent()) existing.setEmail(updatePayLoad.email());
                            if (Optional.ofNullable(updatePayLoad.password()).isPresent()) existing.setPassword(updatePayLoad.password());
                            return userRepository.save(existing);
                        }
                )
                .map(userMapper::userToUserDao)
                .orElseThrow(() -> new UserNotFoundException("user not found for update"));
    }
}
