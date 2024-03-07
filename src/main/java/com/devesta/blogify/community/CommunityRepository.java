package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("select c from Community c inner join c.tags tags where upper(tags.name) like upper(?1)")
    List<Community> findByTags_NameLikeIgnoreCase(String name);

    List<Community> findByNameContainsIgnoreCase(String name);

    @Query("select c.createdBy from Community c where c.communityId = ?1")
    Optional<User> getCommunityOwner(Long cid);

    boolean existsByName(String name);
}