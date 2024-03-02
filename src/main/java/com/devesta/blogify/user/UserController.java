package com.devesta.blogify.user;

import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.post.domain.dto.ListPostDto;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.devesta.blogify.user.domain.UserDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUserV2(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<ListPostDto>> getPostsForUser(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "last_update") String property,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(userService.getUserPosts(username, property, order), HttpStatus.OK);
    }

    @GetMapping("/{username}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsForUser(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "last_update") String property,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(userService.getUserComments(username, property, order), HttpStatus.OK);
    }

    @DeleteMapping("/{cid}/users/{uid}")
    public ResponseEntity<Long> removeUserFromCommunity(
            @PathVariable Long cid,
            @PathVariable Long uid
    ) {
        return new ResponseEntity<>(userService.deleteUserFromCommunity(cid, uid), HttpStatus.OK);
    }

    @PutMapping("{uid}")
    public ResponseEntity<Long> updateUser(
            @RequestBody @Valid UserDto userDto,
            @PathVariable Long uid
    ) {
        Long update = userService.update(userDto, uid);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @PatchMapping("{uid}")
    public ResponseEntity<UserDto> partialUpdate(
            @RequestBody UpdatePayLoad updatePayLoad,
            @PathVariable Long uid
    ) {
        return new ResponseEntity<>(userService.partialUpdate(updatePayLoad,uid), HttpStatus.OK);
    }

}
