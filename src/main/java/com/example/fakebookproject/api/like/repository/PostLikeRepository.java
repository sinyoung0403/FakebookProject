package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.post.entity.Post;
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
        return findByPost_IdAndUser_Id(postId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "데이터 없음"));
    }

    Long post(Post post);
}