package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.common.dto.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponseDto findPostById(Long id);

    PostResponseDto createPost(PostCreateRequestDto postCreateRequestDto, Long loginId);

    PageResponse<PostResponseDto> findMyPost(Long loginId, Pageable pageable);

    PageResponse<PostResponseDto> findRelatedPost(Long loginId, Pageable pageable);

    PostResponseDto updatePost(Long id, Long loginId, PostUpdateDto postUpdateDto);

    void deletePost(Long id, Long loginId);

    PageResponse<PostResponseDto> findPostByUserId(Long id, Pageable pageable);
}
