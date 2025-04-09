package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.CommentLike;
import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findAllByComment_Id(Long commentId);

    Optional<CommentLike> findByComment_IdAndUser_Id(Long postId, Long userId);

    default CommentLike findByComment_IdAndUser_IdOrElseThrow(Long postId, Long userId) {
        return findByComment_IdAndUser_Id(postId, userId).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));
    }

    boolean existsByUser_Id(Long userId);

    default void validateExistenceByUserId(Long userId) {
        if (!existsByUser_Id(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default void validateNotExistenceByUserId(Long userId) {
        if (existsByUser_Id(userId)) {
            throw new CustomException(ExceptionCode.USER_ALREADY_EXISTS);
        }
    }
}
