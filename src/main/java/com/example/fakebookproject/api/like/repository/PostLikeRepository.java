package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findAllByPost_Id(Long postId);

    Optional<PostLike> findByPost_IdAndUser_Id(Long postId, Long userId);

    default PostLike findByPost_IdAndUser_IdOrElseThrow(Long postId, Long userId) {
        return findByPost_IdAndUser_Id(postId, userId).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));
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