package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.CommentLike;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findAllByComment_Id(Long commentId);

    Optional<CommentLike> findByComment_IdAndComment_Post_IdAndUser_Id(Long commentId, Long postId, Long userId);

    /**
     * 주어진 commentId와 userId에 해당하는 엔티티를 조회하고, 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param commentId
     * @param userId
     * @return commentId와 userId 가 일치하는 CommentLike, 존재하지 않으면 예외
     */
    default CommentLike findByCommentOrElseThrow(Long commentId, Long postId, Long userId) {
        return findByComment_IdAndComment_Post_IdAndUser_Id(commentId, postId, userId).orElseThrow(() -> new CustomException(ExceptionCode.LIKE_FAILED));
    }

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    /**
     * userId 가 존재하는지 확인하기 위한 메서드
     * Comment Like Repository 에 주어진 userId를 가진 엔티티가 존재하지 않으면 예외 발생
     *
     * @param userId
     */
    default void validateExistenceByUserIdAndCommentId(Long userId, Long commentId) {
        if (!existsByUserIdAndCommentId(userId, commentId)) {
            throw new CustomException(ExceptionCode.LIKE_USER_NOT_FOUND);
        }
    }

    /**
     * userId 가 존재하지 않는 걸 확인하기 위한 메서드
     * Comment Like Repository 에 주어진 userId를 가진 엔티티가 존재하면 예외 발생
     *
     * @param userId
     */
    default void validateNotExistenceByUserIdAndCommentId(Long userId, Long commentId) {
        if (existsByUserIdAndCommentId(userId, commentId)) {
            throw new CustomException(ExceptionCode.ALREADY_LIKE);
        }
    }
}
