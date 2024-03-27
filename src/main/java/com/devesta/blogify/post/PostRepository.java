package com.devesta.blogify.post;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.post.domain.ListPostDto;
import com.devesta.blogify.post.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.community.communityId = ?1")
    List<Post> findByCommunity_CommunityId(Long communityId);

    @Query("SELECT NEW com.devesta.blogify.post.domain.ListPostDto(" +
            "p.postId, p.author.username, p.title, p.body, " +
            "p.votes, p.lastUpdate) " +
            "FROM Post p " +
            "WHERE p.postId = :id")
    Optional<ListPostDto> findPostDetail(@Param("id") Long id);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
    List<Comment> findCommentsByPostId(Long postId);

    @Query(
            value = "SELECT COUNT(*) FROM comment WHERE post_id = ?1",
            nativeQuery = true
    )
    int getNumberOfComments(Long pid);

    List<Post> findByTitleContainsIgnoreCaseAndLastUpdateAfter(String title, LocalDate date, Sort sort);

    @Query("SELECT p FROM Post p WHERE lower(p.title) LIKE lower(concat('%', :title, '%')) " +
            "AND p.lastUpdate > :date ORDER BY random()")
    List<Post> findRandomByTitleContainsIgnoreCaseAndLastUpdateAfter(
            @Param("title") String title,
            @Param("date") LocalDate date
    );

    List<Post> findByAuthor_userId(Long id, Sort sort);

    @Transactional
    @Modifying
    @Query("update Post p set p.votes = p.votes + 1 where p.postId = ?1")
    int updateVotesByPostIdPlus(Long postId);

    @Transactional
    @Modifying
    @Query("update Post p set p.votes = p.votes - 1 where p.postId = ?1 and p.votes > 0")
    int updateVotesByPostIdMinus(Long postId);


}