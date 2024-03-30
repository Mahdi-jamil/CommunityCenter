package com.devesta.blogify.user;

import com.devesta.blogify.comment.CommentRepository;
import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.comment.domain.CommentMapper;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.notfound.UserNotFoundException;
import com.devesta.blogify.firebase.FileDAO;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.devesta.blogify.user.domain.User;
import com.devesta.blogify.user.domain.userdetial.DetailUserMapper;
import com.devesta.blogify.user.domain.userdetial.FullDetailUser;
import com.devesta.blogify.user.domain.userlist.SimpleUserMapper;
import com.devesta.blogify.user.domain.userlist.UserDto;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SimpleUserMapper simpleUserMapper;
    private final PostRepository postRepository;
    private final ListPostMapper listPostMapper;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentMapper commentMapper;
    private final ListCommunityMapper listCommunityMapper;
    private final FileDAO fileDAO;
    private final DetailUserMapper detailUserMapper;

    public List<UserDto> getAllUser() {
        return userRepository
                .findAll(PageRequest.ofSize(10))
                .stream()
                .map(simpleUserMapper::userToUserDao)
                .collect(Collectors.toList());
    }

    public FullDetailUser getDetailedUser(Long uid) {
        return userRepository.findById(uid)
                .map(detailUserMapper::userToDetailUserDao)
                .orElseThrow(() -> new UserNotFoundException("user with id:" + uid + " not found"));
    }

    public FullDetailUser getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(detailUserMapper::userToDetailUserDao)
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

    public List<ListCommunityDto> getUserCommunities(Long uid) {
        return userRepository.userCommunities(uid)
                .stream()
                .map(listCommunityMapper::COMMUNITY_DTO)
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

        return simpleUserMapper.userToUserDao(userRepository.save(user));

    }

    public String getUserProfileUrl(Long uid) {
        return userRepository.findProfileUrlById(uid);
    }

    public String uploadProfileImage(@NotNull Authentication authentication, MultipartFile image) throws IOException {
        String image_url = fileDAO.uploadAndGetUrl(image);
        User user = (User) authentication.getPrincipal();

        String oldImageUrl = user.getProfileUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            fileDAO.deleteFileFromFirebase(oldImageUrl);
        }

        user.setProfileUrl(image_url);
        userRepository.save(user);

        return image_url;
    }

}
