package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PostService {

    void createPost(PostCreateRequestDto postCreateRequestDto, HttpServletRequest request);
}
