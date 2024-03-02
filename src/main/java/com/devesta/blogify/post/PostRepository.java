package com.devesta.blogify.post;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.post.domain.dto.PostDetailDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT NEW com.devesta.blogify.post.domain.dto.PostDetailDto(" +
            "p.postId, p.author.username, p.title, p.body, " +
            "p.votes, p.lastUpdate) " +
            "FROM Post p " +
            "WHERE p.postId = :id")
    Optional<PostDetailDto> findPostDetail(@Param("id") Long id);

    @Query("select p from Post p where p.author.username = ?1")
    List<Post> findByAuthor_Username(String username, Sort sort);


}