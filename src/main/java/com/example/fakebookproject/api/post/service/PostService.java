package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    PostResponseDto findPostById(Long id);

    void createPost(PostCreateRequestDto postCreateRequestDto, HttpServletRequest request);

    Page<PostResponseDto> findMyPost(Long loginId, int page, int size);

    Page<PostResponseDto> findRelatedPost(Long loginId, int page, int size);
}
