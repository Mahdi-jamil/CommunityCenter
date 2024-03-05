package com.devesta.blogify.comment;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.post.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost_PostId(Long postId);

    List<Comment> findByParentComment_CommentId(Long parentId);

    @Query("select c.post from Comment c where c.commentId = ?1")
    Optional<Post> findByCommentId(Long commentId);

    List<Comment> findByAuthor_userId(Long id, Sort sort);

}