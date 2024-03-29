package com.devesta.blogify.user;

import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.user.domain.userdetial.FullDetailUser;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.devesta.blogify.user.domain.userlist.UserDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{uid}/posts")
    public ResponseEntity<List<ListPostDto>> getPostsForUser(
            @PathVariable Long uid,
            @RequestParam(required = false, defaultValue = "lastUpdate") String property,
            @RequestParam(required = false, defaultValue = "desc") String order
    ) {
        return new ResponseEntity<>(userService.getUserPosts(uid, property, order), HttpStatus.OK);
    }

    @GetMapping("/{uid}/communities")
    public ResponseEntity<List<ListCommunityDto>> getUserCommunities(@PathVariable Long uid) {
        return new ResponseEntity<>(userService.getUserCommunities(uid), HttpStatus.OK);
    }


    @GetMapping("/{uid}")
    public ResponseEntity<FullDetailUser> getDetailedUser(@PathVariable Long uid) {
        return new ResponseEntity<>(userService.getDetailedUser(uid), HttpStatus.OK);
    }


    @GetMapping("/{uid}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsForUser(
            @PathVariable Long uid,
            @RequestParam(required = false, defaultValue = "lastUpdate") String property,
            @RequestParam(required = false, defaultValue = "desc") String order
    ) {
        return new ResponseEntity<>(userService.getUserComments(uid, property, order), HttpStatus.OK);
    }


    @PatchMapping("/{uid}")
    public ResponseEntity<UserDto> partialUpdate(
            @RequestBody @Valid UpdatePayLoad updatePayLoad,
            @PathVariable Long uid
    ) {
        return new ResponseEntity<>(userService.partialUpdate(updatePayLoad, uid), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(
            @RequestPart MultipartFile file,
            Authentication authentication) throws IOException {
        return new ResponseEntity<>(userService.uploadProfileImage(authentication, file),HttpStatus.OK);
    }

    @GetMapping("/{uid}/profile")
    public ResponseEntity<String> getUserProfileUrl(@PathVariable Long uid) {
        return new ResponseEntity<>(userService.getUserProfileUrl(uid), HttpStatus.OK);
    }
}
