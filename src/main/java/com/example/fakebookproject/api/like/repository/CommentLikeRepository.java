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

    /**
     * 주어진 commentId와 userId에 해당하는 엔티티를 조회하고, 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param postId
     * @param userId
     * @return commentId와 userId 가 일치하는 CommentLike, 존재하지 않으면 예외
     */
    default CommentLike findByComment_IdAndUser_IdOrElseThrow(Long postId, Long userId) {
        return findByComment_IdAndUser_Id(postId, userId).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));
    }

    boolean existsByUser_Id(Long userId);

    /**
     * userId 가 존재하는지 확인하기 위한 메서드
     * Comment Like Repository 에 주어진 userId를 가진 엔티티가 존재하지 않으면 예외 발생
     *
     * @param userId
     */
    default void validateExistenceByUserId(Long userId) {
        if (!existsByUser_Id(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    /**
     * userId 가 존재하지 않는 걸 확인하기 위한 메서드
     * Comment Like Repository 에 주어진 userId를 가진 엔티티가 존재하면 예외 발생
     *
     * @param userId
     */
    default void validateNotExistenceByUserId(Long userId) {
        if (existsByUser_Id(userId)) {
            throw new CustomException(ExceptionCode.USER_ALREADY_EXISTS);
        }
    }
}
