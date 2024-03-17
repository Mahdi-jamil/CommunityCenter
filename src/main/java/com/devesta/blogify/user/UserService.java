package com.devesta.blogify.user;

import com.devesta.blogify.comment.CommentRepository;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.BadRequestException;
import com.devesta.blogify.exception.exceptions.UserNotFoundException;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.user.domain.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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
        Sort sort = Sort.by(Sort.Direction.fromString(order), property);
        return postRepository
                .findByAuthor_userId(uid, sort)
                .stream()
                .map(listPostMapper::postToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getUserComments(Long uid, String property, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), property);
        return commentRepository
                .findAllByAuthor_userId(uid, sort)
                .stream()
                .map(commentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    public UserDto partialUpdate(UpdatePayLoad updatePayLoad, Long uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found for update"));

        Optional.ofNullable(updatePayLoad.email())
                .ifPresent(user::setEmail);
        Optional.ofNullable(updatePayLoad.password())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
        Optional.ofNullable(updatePayLoad.bio())
                .ifPresent(user::setBio);

        return userMapper.userToUserDao(userRepository.save(user));

    }

    public List<ListCommunityDto> getUserCommunities(Long uid) {
        return userRepository.userCommunities(uid)
                .stream()
                .map(listCommunityMapper::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    public void uploadProfileImage(Authentication authentication, MultipartFile file) {
        User user = (User) authentication.getPrincipal();
        try {
            Blob profileImg = new SerialBlob(file.getBytes());
            if (!isImage(file)) throw new BadRequestException("Only .jpeg, .jpg, or .png image files are allowed.");
            user.setProfileImage(new ProfileImage(user.getUserId(), profileImg));
            userRepository.save(user);
        } catch (IOException | SQLException e) {
            throw new BadRequestException("Upload correct file");
        }
    }

    private boolean isImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png");
    }

    public Blob getUserProfile(Long uid) {
        return userRepository.getUserProfileImg(uid);
    }
}
