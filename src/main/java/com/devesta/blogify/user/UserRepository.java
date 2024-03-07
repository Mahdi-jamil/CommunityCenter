package com.devesta.blogify.user;

import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainsIgnoreCase(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            select u from User u inner join u.joinedCommunities joinedCommunities
            where joinedCommunities.communityId = ?1""")
    List<User> findByJoinedCommunities_CommunityId(Long communityId);

    @Query("select u.joinedCommunities from User u where u.userId = ?1")
    List<Community> userCommunities(Long uid);

    @Modifying
    @Transactional
    @Query(
            value = "insert into user_community (user_id, community_id) values (?1, ?2)",
            nativeQuery = true
    )
    void joinCommunity(Long uid, Long cid);

    @Modifying
    @Transactional
    @Query(
            value = "delete from user_community where user_id = ?1 and community_id = ?2",
            nativeQuery = true
    )
    void leaveCommunity(Long uid, Long cid);

    @Query(
            value = "select user_id from user_community where user_id = ?1 and community_id = ?2",
            nativeQuery = true
    )
    Long alreadyJoined(Long uid, Long cid);

}

