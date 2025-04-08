package com.example.fakebookproject.api.comment.service;

import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentResponseDto createComment(Long userId, Long postId, String content) {
        return null;
    }

    @Override
    public Page<CommentResponseDto> findAllComments(Long userId, Long postId, Pageable pageable) {
        return null;
    }

    @Override
    public CommentResponseDto updateComment(Long userId, Long commentId, String content) {
        return null;
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {

    }
}
