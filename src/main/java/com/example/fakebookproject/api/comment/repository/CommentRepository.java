package com.example.fakebookproject.api.comment.repository;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);
}
