package com.devesta.blogify.community;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.user.domain.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {


    // Lazy loading overridden with eager fetch for posts
    @Query("SELECT c FROM Community c LEFT JOIN FETCH c.posts WHERE c.communityId = :id")
    Optional<Community> findByIdWithPostsEagerly(@Param("id") Long id);

    @Query("select c from Community c inner join c.tags tags where upper(tags.name) like upper(?1)")
    List<Community> findByTags_NameLikeIgnoreCase(String name);

}