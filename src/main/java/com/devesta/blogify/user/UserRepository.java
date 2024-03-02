package com.devesta.blogify.user;

import com.devesta.blogify.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            select u from User u inner join u.joinedCommunities joinedCommunities
            where joinedCommunities.communityId = ?1""")
    List<User> findByJoinedCommunities_CommunityId(Long communityId);

    @Modifying
    @Query(
            value = "delete from user_community where community_id = :cid and user_id = :uid;",
            nativeQuery = true
    )
    Long deleteUserFromCommunity(Long cid, Long uid);

    @Modifying
    @Transactional // todo make above service method
    @Query(
            value = "update users set email = :email where id = :uid",
            nativeQuery = true
    )
    int updateEmail(
            @Param("email") String emial,
            @Param("uid") Long id
    );

}

