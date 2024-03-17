package com.devesta.blogify.search;

import com.devesta.blogify.comment.domain.CommentDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.user.UserService;
import com.devesta.blogify.user.domain.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/posts/{title}")
    public ResponseEntity<List<ListPostDto>> searchPostsByTitle(
            @PathVariable String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate postedAfter,
            @RequestParam(required = false) String property,
            @RequestParam(required = false, defaultValue = "desc") String order
    ) {
        return new ResponseEntity<>(searchService.searchForPost(title, postedAfter, property, order), HttpStatus.OK);
    }

    @GetMapping("/communities/{name}")
    public ResponseEntity<List<ListCommunityDto>> searchCommunitiesByName(@PathVariable String name) {
        return new ResponseEntity<>(searchService.searchForCommunities(name), HttpStatus.OK);
    }

    @GetMapping("/comment/{body}")
    public ResponseEntity<List<CommentDto>> searchCommentsContaining(
            @RequestParam(required = false) String property,
            @PathVariable String body) {
        return new ResponseEntity<>(searchService.searchForComments(body, property), HttpStatus.OK);
    }

    @GetMapping("/people/{username}")
    public ResponseEntity<List<UserDto>> searchUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(searchService.searchForPeople(username), HttpStatus.OK);
    }

}
