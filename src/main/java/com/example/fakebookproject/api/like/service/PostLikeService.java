package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;

import java.util.List;

public interface PostLikeService {

    public void createPostLike(Long postId);

    List<FindAllPostLikeResponseDto> findAllLike(Long postId);
}
