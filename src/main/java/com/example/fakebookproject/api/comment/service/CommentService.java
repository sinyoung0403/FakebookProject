package com.example.fakebookproject.api.comment.service;

import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(Long userId, Long postId, String content);

    PageResponse<CommentResponseDto> findAllComments(Long postId, Pageable pageable);

    CommentResponseDto updateComment(Long userId, Long postId, Long commentId, String content);

    void deleteComment(Long userId, Long postId, Long commentId);

}
