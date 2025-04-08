package com.example.fakebookproject.api.comment.service;

import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(Long userId, Long postId, String content);

    Page<CommentResponseDto> findAllComments(Long postId, Pageable pageable);

    CommentResponseDto updateComment(Long userId, Long commentId, String content);

    void deleteComment(Long userId, Long commentId);

}
