package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.dto.CommunityDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.PostDto;
import com.devesta.blogify.user.domain.userlist.UserDto;
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
@RequestMapping("/api/v1/communities")
public class CommunityController {

    private final CommunityService communityService;

    /**
     * Retrieves a list of all communities.
     *
     * @return ResponseEntity<List<ListCommunityDto>> List of communities.
     */
    @GetMapping
    public ResponseEntity<List<ListCommunityDto>> getAllCommunities() {
        return new ResponseEntity<>(communityService.getListOfCommunities(), HttpStatus.OK);
    }

    /**
     * Retrieves detailed information about a specific community.
     *
     * @param cid ID of the community.
     * @return ResponseEntity<ListCommunityDto> Detailed information about the community.
     */
    @GetMapping("{cid}")
    public ResponseEntity<ListCommunityDto> getFullDetailedCommunity(@PathVariable Long cid) {
        return new ResponseEntity<>(communityService.detailCommunityDto(cid), HttpStatus.OK);
    }

    /**
     * Creates a new community.
     *
     * @param communityDto      Details of the community to be created.
     * @param authentication    Authentication object for the user creating the community.
     * @return ResponseEntity<CommunityDto> Details of the newly created community.
     */
    @PostMapping
    public ResponseEntity<CommunityDto> createNewCommunity(
            @RequestBody @Valid CommunityDto communityDto,
            Authentication authentication) {
        return new ResponseEntity<>(communityService.addCommunity(communityDto, authentication), HttpStatus.CREATED);
    }

    /**
     * Retrieves posts within a community.
     *
     * @param cid ID of the community.
     * @return ResponseEntity<List<ListPostDto>> List of posts in the community.
     */
    @GetMapping("{cid}/posts")
    public ResponseEntity<List<ListPostDto>> getCommunityPosts(@PathVariable Long cid) {
        return new ResponseEntity<>(communityService.getCommunityPosts(cid), HttpStatus.OK);
    }

    /**
     * Adds a post to a community.
     *
     * @param cid              ID of the community.
     * @param postDto          Details of the post to be added.
     * @param authentication   Authentication object for the user adding the post.
     * @return ResponseEntity<ListPostDto> Details of the added post.
     */
    @PostMapping("{cid}/posts")
    public ResponseEntity<ListPostDto> addPostToCommunity(
            @PathVariable Long cid,
            @RequestBody PostDto postDto,
            Authentication authentication
    ) {
        return new ResponseEntity<>(communityService.addPost(cid, postDto, authentication), HttpStatus.OK);
    }

    /**
     * Retrieves a list of communities by tag.
     *
     * @param tagName Tag name for filtering communities.
     * @return ResponseEntity<List<ListCommunityDto>> List of communities filtered by tag.
     */
    @GetMapping("tags/{tagName}")
    public ResponseEntity<List<ListCommunityDto>> getAllCommunitiesByTag(@PathVariable String tagName) {
        return new ResponseEntity<>(communityService.getListOfCommunitiesByTag(tagName), HttpStatus.OK);
    }

    /**
     * Allows a user to join a community.
     *
     * @param cid              ID of the community.
     * @param authentication   Authentication object for the user joining the community.
     * @return ResponseEntity<Void> Response indicating the success of the operation.
     */
    @PostMapping("/{cid}/join")
    public ResponseEntity<Void> joinCommunity(
            @PathVariable Long cid,
            Authentication authentication
    ) {
        communityService.joinCommunity(cid, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Allows a user to leave a community.
     *
     * @param cid              ID of the community.
     * @param authentication   Authentication object for the user leaving the community.
     * @return ResponseEntity<Void> Response indicating the success of the operation.
     */
    @DeleteMapping("/{cid}/leave")
    public ResponseEntity<Void> removeUserFromCommunity(
            @PathVariable Long cid,
            Authentication authentication
    ) {
        communityService.leaveCommunity(cid, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a community.
     *
     * @param cid              ID of the community.
     * @param authentication   Authentication object for the user deleting the community.
     * @return ResponseEntity<Void> Response indicating the success of the operation.
     */
    @DeleteMapping("{cid}")
    public ResponseEntity<Void> deleteCommunity(
            @PathVariable Long cid,
            Authentication authentication) {
        communityService.deleteCommunity(cid, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{communityId}/moderators/{userId}")
    public ResponseEntity<?> addModeratorToCommunity(
            @PathVariable Long communityId,
            @PathVariable Long userId,
            Authentication authentication) {
        communityService.promoteToModerator(communityId, userId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}/moderators/{userId}")
    public ResponseEntity<?> removeModeratorFromCommunity(
            @PathVariable Long communityId,
            @PathVariable Long userId,
            Authentication authentication) {
        communityService.removeModeratorFromCommunity(communityId, userId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}/{userId}")
    public ResponseEntity<?> bannedUser(
            @PathVariable Long communityId,
            @PathVariable Long userId,
            Authentication authentication) {
        communityService.bannedMember(communityId, userId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}/moderator")
    public ResponseEntity<List<UserDto>> getModerators(@PathVariable Long communityId){
        return new ResponseEntity<>(communityService.getModerators(communityId), HttpStatus.OK) ;
    }

    @PostMapping(value = "/{cid}/upload_icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadIcon(
            @PathVariable Long cid,
            @RequestPart MultipartFile file,
            Authentication authentication) throws IOException {
        return new ResponseEntity<>(communityService.uploadCommunityIcon(cid,authentication, file),HttpStatus.OK);
    }

    @GetMapping("/{cid}/icon")
    public ResponseEntity<String> getUserProfileUrl(@PathVariable Long cid) {
        return new ResponseEntity<>(communityService.getCommunityIcon(cid), HttpStatus.OK);
    }

}
