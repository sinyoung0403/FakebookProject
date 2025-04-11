package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findAllByPost_Id(Long postId);

    Optional<PostLike> findByPost_IdAndUser_Id(Long postId, Long userId);

    /**
     * 주어진 postId와 userId에 해당하는 엔티티를 조회하고, 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param postId
     * @param userId
     * @return postId와 userId 가 일치하는 PostLike, 존재하지 않으면 예외
     */
    default PostLike findByPost_IdAndUser_IdOrElseThrow(Long postId, Long userId) {
        return findByPost_IdAndUser_Id(postId, userId).orElseThrow(
                () -> new CustomException(ExceptionCode.LIKE_FAILED));
    }

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * userId 가 존재하는지 확인하기 위한 메서드
     * Post Like Repository 에 주어진 userId를 가진 엔티티가 존재하지 않으면 예외 발생
     *
     * @param userId
     */
    default void validateExistenceByUserIdAndPostId(Long userId, Long postId) {
        if (!existsByUserIdAndPostId(userId, postId)) {
            throw new CustomException(ExceptionCode.LIKE_USER_NOT_FOUND);
        }
    }
    /**
     * userId 가 존재하지 않는 걸 확인하기 위한 메서드
     * Post Like Repository 에 주어진 userId를 가진 엔티티가 존재하면 예외 발생
     *
     * @param userId
     */
    default void validateNotExistenceByUserIdAndPostId(Long userId, Long postId) {
        if (existsByUserIdAndPostId(userId, postId)) {
            throw new CustomException(ExceptionCode.ALREADY_LIKE);
        }
    }
}