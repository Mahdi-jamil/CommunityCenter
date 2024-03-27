package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.community.domain.dto.CommunityDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.*;
import com.devesta.blogify.post.PostRepository;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.ListPostMapper;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.post.domain.PostDto;
import com.devesta.blogify.tag.Tag;
import com.devesta.blogify.tag.TagDto;
import com.devesta.blogify.tag.TagRepository;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.User;
import com.devesta.blogify.user.domain.userlist.SimpleUserMapper;
import com.devesta.blogify.user.domain.userlist.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ListCommunityMapper listCommunityMapper;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final ListPostMapper listPostMapper;
    private final SimpleUserMapper simpleUserMapper;

    public ListCommunityDto detailCommunityDto(Long cid) {
        return communityRepository.findById(cid)
                .map(listCommunityMapper::COMMUNITY_DTO)
                .orElseThrow(() -> new CommunityNotFoundException("community trying to see not found"));
    }

    public List<ListCommunityDto> getListOfCommunities() {
        return communityRepository
                .findAll()
                .stream()
                .map(listCommunityMapper.INSTANCE::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    public List<ListCommunityDto> getTopCommunities() {
        return communityRepository
                .findAll(PageRequest.of(4, 20, Sort.by("numberOfMembers")))
                .stream()
                .map(listCommunityMapper.INSTANCE::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    public List<ListCommunityDto> getListOfCommunitiesByTag(String tagName) {
        return communityRepository
                .findByTags_NameLikeIgnoreCase(tagName)
                .stream()
                .map(listCommunityMapper.INSTANCE::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    private Set<TagDto> tagToDto(Set<Tag> tags) {
        Set<TagDto> tagDto = new HashSet<>();
        for (Tag tag : tags) {
            tagDto.add(new TagDto(tag.getName()));
        }
        return tagDto;
    }

    public CommunityDto addCommunity(CommunityDto communityDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (communityRepository.existsByName(communityDto.name()))
            throw new CommunityNameExistException("community with name: " + communityDto.name() + " already exist");
        HashSet<User> moderators = new HashSet<>();
        moderators.add(user);

        Community community = new Community();
        community.setName(communityDto.name());
        community.setDescription(communityDto.description());
        community.setModerators(moderators);
        community.setNumberOfMembers(1);
        community.setCreator(user);

        Set<Tag> uniqueTags = new HashSet<>();
        for (TagDto tag : communityDto.tags()) {
            if (tagRepository.existsByName(tag.name())) {
                uniqueTags.add(tagRepository.findByName(tag.name()));
            } else {
                Tag newTag = new Tag();
                newTag.setName(tag.name());
                uniqueTags.add(tagRepository.save(newTag));
            }
        }

        community.setTags(uniqueTags);
        Community saved = communityRepository.save(community);

        return new CommunityDto(
                saved.getName(),
                saved.getDescription(),
                tagToDto(saved.getTags())
        );
    }

    public void joinCommunity(Long cid, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (userRepository.alreadyJoined(user.getUserId(), cid) != null)
            throw new UserAlreadyJoinedException("User already in this community");

        userRepository.joinCommunity(user.getUserId(), cid);
    }

    public void leaveCommunity(Long cid, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (userRepository.alreadyJoined(user.getUserId(), cid) == null)
            throw new UserNotJoinedException("Not member to leave");

        userRepository.leaveCommunity(user.getUserId(), cid);
    }

    public ListPostDto addPost(Long cid, PostDto postDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (userRepository.alreadyJoined(user.getUserId(), cid) == null)
            throw new UserNotJoinedException("Join the community first to post.");

        Post post = new Post();
        post.setAuthor(user);
        post.setBody(postDto.body());
        post.setTitle(postDto.title());
        post.setVotes(0);
        post.setCommunity(communityRepository.findById(cid).orElseThrow(
                () -> new CommunityNotFoundException("community not found to post in it")));

        Post saved = postRepository.save(post);
        return listPostMapper.postToDto(saved);
    }

    public List<ListPostDto> getCommunityPosts(Long cid) {
        return postRepository
                .findByCommunity_CommunityId(cid)
                .stream()
                .map(listPostMapper::postToDto)
                .collect(Collectors.toList());
    }

    public void deleteCommunity(Long cid, Authentication authentication) {
        validateOwnerShip(cid, authentication, "User is not authorized to delete this community");
        communityRepository.deleteById(cid);
    }

    public void promoteToModerator(Long communityId, Long userId, Authentication authentication) {
        Community community =
                validateOwnerShip(communityId, authentication, "User is not authorized to promote to moderators");
        User userToPromote = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user to be promoted not found "));

        community.getModerators().add(userToPromote);
        communityRepository.save(community);
    }

    public void removeModeratorFromCommunity(Long communityId, Long userId, Authentication authentication) {
        Community community =
                validateOwnerShip(communityId, authentication, "User is not authorized to demote to moderators");
        User userToDemote = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user to be promoted not found "));

        if (community.getModerators().contains(userToDemote)) {
            community.getModerators().remove(userToDemote);
            communityRepository.save(community);
        }
    }

    public void bannedMember(Long communityId, Long userId, Authentication authentication) {
        Community community =
                validateOwnerShip(communityId, authentication, "User is not authorized to banned a member");
        User userToBanned = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user to be promoted not found "));

        userToBanned.getJoinedCommunities().remove(community);
        userRepository.save(userToBanned);
    }

    private Community validateOwnerShip(Long cid, Authentication authentication, String authorizeMessage) {
        Community community = communityRepository.findById(cid)
                .orElseThrow(() -> new CommunityNotFoundException("community trying to modify not found"));
        User user = (User) authentication.getPrincipal();
        Optional<User> owner = communityRepository.getCommunityOwner(cid);

        if (owner.isEmpty() || !owner.get().equals(user))
            throw new UnauthorizedAccessException(authorizeMessage);
        return community;
    }

    public List<UserDto> getModerators(Long communityId) {
        return communityRepository.getCommunityModerators(communityId)
                .stream()
                .map(simpleUserMapper::userToUserDao)
                .collect(Collectors.toList());
    }
}
