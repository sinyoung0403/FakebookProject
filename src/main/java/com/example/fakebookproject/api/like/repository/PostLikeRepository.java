package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findAllByPost_Id(Long postId);
}
