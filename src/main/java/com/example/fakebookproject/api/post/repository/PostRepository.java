package com.example.fakebookproject.api.post.repository;

import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE)
        );
    }
}
