package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
