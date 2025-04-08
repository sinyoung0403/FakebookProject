package com.example.fakebookproject.api.post.repository;

import com.example.fakebookproject.api.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
