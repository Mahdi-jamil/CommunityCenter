package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.community.domain.dto.CommunityDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.CommunityNameExistException;
import com.devesta.blogify.exception.exceptions.CommunityNotFoundException;
import com.devesta.blogify.exception.exceptions.UserNotJoinedException;
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
import com.devesta.blogify.user.domain.UserDto;
import com.devesta.blogify.user.domain.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ListCommunityMapper listCommunityMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final ListPostMapper listPostMapper;

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

    public List<ListCommunityDto> getListOfCommunitiesByTag(String tagName) {
        return communityRepository
                .findByTags_NameLikeIgnoreCase(tagName)
                .stream()
                .map(listCommunityMapper.INSTANCE::COMMUNITY_DTO)
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersInCommunity(Long cid) {
        if (!communityRepository.existsById(cid)) {
            throw new CommunityNotFoundException("community not found");
        }

        List<User> userList = userRepository.findByJoinedCommunities_CommunityId(cid);
        return userList.stream()
                .map(userMapper.INSTANCE::userToUserDao)
                .collect(Collectors.toList());
    }

    private List<TagDto> tagToDto(List<Tag> tags) {
        List<TagDto> tagDto = new ArrayList<>();
        for (Tag tag : tags) {
            tagDto.add(new TagDto(tag.getName()));
        }
        return tagDto;
    }

    public CommunityDto addCommunity(CommunityDto communityDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (communityRepository.existsByName(communityDto.name()))
            throw new CommunityNameExistException("community with name: " + communityDto.name() + " already exist");

        Community community = new Community();
        community.setName(communityDto.name());
        community.setDescription(communityDto.description());
        community.setNumberOfMembers(1);
        community.setCreatedBy(user);

        List<Tag> uniqueTags = new ArrayList<>();
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
        if (userRepository.alreadyJoined(user.getUserId(), cid) != null) {
            return;
        }
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
        communityRepository.findById(cid)
                .orElseThrow(() -> new CommunityNotFoundException("community trying to delete not found"));
        User user = (User) authentication.getPrincipal();
        Optional<User> owner = communityRepository.getCommunityOwner(cid);

        if (owner.isPresent() && owner.get().equals(user)) {
            communityRepository.deleteById(cid);
            return;
        }

        throw new BadCredentialsException("cannot delete other's community");
    }
}
