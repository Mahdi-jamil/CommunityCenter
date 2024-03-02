package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.dto.FullDetailCommunityDto;
import com.devesta.blogify.community.domain.dto.ListCommunityDto;
import com.devesta.blogify.community.domain.mapper.FullDetailCommunityMapper;
import com.devesta.blogify.community.domain.mapper.ListCommunityMapper;
import com.devesta.blogify.exception.exceptions.CommunityNotFoundException;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.User;
import com.devesta.blogify.user.domain.UserDto;
import com.devesta.blogify.user.domain.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ListCommunityMapper listCommunityMapper;
    private final FullDetailCommunityMapper fullDetailCommunityMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    public FullDetailCommunityDto detailCommunityDto(Long id) {
        return communityRepository.findByIdWithPostsEagerly(id)
                .map(fullDetailCommunityMapper.INSTANCE::communityToCommunityDto)
                .orElseThrow(() -> new CommunityNotFoundException("community not found"));
    }

    public List<UserDto> getUsersInCommunity(Long cid) {
        List<User> userList = userRepository.findByJoinedCommunities_CommunityId(cid);
        return userList.stream()
                .map(userMapper.INSTANCE::userToUserDao)
                .collect(Collectors.toList());
    }


}
