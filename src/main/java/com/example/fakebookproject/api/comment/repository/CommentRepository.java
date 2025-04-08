package com.example.fakebookproject.api.comment.repository;

import com.example.fakebookproject.api.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
