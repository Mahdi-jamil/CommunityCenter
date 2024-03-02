package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.dto.FullDetailCommunityDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/{cid}/users")
    public ResponseEntity<List<UserDto>> getUsersInCommunity(@PathVariable Long cid) {
        return new ResponseEntity<>(communityService.getUsersInCommunity(cid), HttpStatus.OK);
    }

    @GetMapping("{cid}")
    public ResponseEntity<FullDetailCommunityDto> getFullDetailedCommunity(@PathVariable Long cid) {
        return new ResponseEntity<>(communityService.detailCommunityDto(cid), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ListCommunityDto>> getAllCommunities() {
        return new ResponseEntity<>(communityService.getListOfCommunities(), HttpStatus.OK);
    }

    @GetMapping("tags/{tagName}")
    public ResponseEntity<List<ListCommunityDto>> getAllCommunitiesByTag(@PathVariable String tagName) {
        return new ResponseEntity<>(communityService.getListOfCommunitiesByTag(tagName), HttpStatus.OK);
    }

}
