package com.example.fakebookproject.api.like.repository;

import com.example.fakebookproject.api.like.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
