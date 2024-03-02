package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.post.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    List<Comment> findByParentComment_CommentId(Long parentId);

    @Query("select c.post from Comment c where c.commentId = ?1")
    Optional<Post> findByCommentId(Long commentId);

    @Query("select c from Comment c where c.author.username = ?1")
    List<Comment> findByAuthor_Username(String username, Sort sort);

    @Query("select c from Comment c where c.post.postId = ?1")
    List<Comment> findByPost_PostId(Long postId);

    //todo get 5 comments on a comment


}