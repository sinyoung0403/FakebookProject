package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;

import java.util.List;

public interface PostLikeService {

    void createPostLike(Long postId, LoginRequestDto loginRequestDto);

    List<FindAllPostLikeResponseDto> findAllPostLike(Long postId);

    void deletePostLike(Long postId);
}
