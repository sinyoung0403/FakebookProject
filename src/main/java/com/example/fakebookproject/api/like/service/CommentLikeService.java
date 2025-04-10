package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;

import java.util.List;

public interface CommentLikeService {
    void createCommentLike(Long postId, Long commentId, Long loginUserId);

    List<FindAllCommentLikeResponseDto> findAllCommentLike(Long postId, Long commentId, Long loginUserId);

    void deleteCommentLike(Long postId, Long commentId, Long loginUserId);
}
