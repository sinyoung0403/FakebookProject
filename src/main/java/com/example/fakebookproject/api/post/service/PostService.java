package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.common.dto.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    PostResponseDto findPostById(Long id);

    void createPost(PostCreateRequestDto postCreateRequestDto, Long loginId);

    PageResponse<PostResponseDto> findMyPost(Long loginId, int page, int size);

    PageResponse<PostResponseDto> findRelatedPost(Long loginId, int page, int size);

    void updatePost(Long id, Long loginId, PostUpdateDto postUpdateDto);

    void deletePost(Long id, Long loginId);
}
