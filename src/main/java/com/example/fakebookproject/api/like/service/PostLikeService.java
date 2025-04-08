package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;

import java.util.List;

public interface PostLikeService {

    void createPostLike(Long postId);

    List<FindAllPostLikeResponseDto> findAllPostLike(Long postId);

    void deletePostLike(Long postId);
}
