package com.example.fakebookproject.api.comment.repository;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    boolean existsById(Long commentId);

    Page<Comment> findByPost(Post post, Pageable pageable);

    default Comment findCommentByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );
    }
    default void validateNotExistenceByCommentId(Long commentId) {
        if (!existsById(commentId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_COMMENT);
        }
    }

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void increaseLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId")
    void decreaseLikeCount(@Param("commentId") Long commentId);
}
