package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.user.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBodyContainsIgnoreCase(String body, Sort sort);
    List<Comment> findAllByBodyContainsIgnoreCase(String body);

    List<Comment> findAllByPost_PostId(Long postId);

    @Query("select c.author from Comment c where c.commentId = ?1")
    Optional<User> findUserByCommentId(Long commentId);

    List<Comment> findAllByPost_PostIdAndParentComment_CommentId(Long postId, Long commentId);

    List<Comment> findAllByAuthor_userId(Long id, Sort sort);

}