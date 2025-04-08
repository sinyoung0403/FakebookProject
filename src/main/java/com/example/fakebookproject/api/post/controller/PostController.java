package com.example.fakebookproject.api.post.controller;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
            @RequestBody @Valid PostCreateRequestDto requestDto,
            HttpServletRequest request
    )
    {
        postService.createPost(requestDto, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<PostResponseDto>> findMyPost(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){

        Page<PostResponseDto> responseDto = postService.findMyPost(request, page, size);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findRelatedPost(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<PostResponseDto> responseDto = postService.findRelatedPost(request, page, size);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
