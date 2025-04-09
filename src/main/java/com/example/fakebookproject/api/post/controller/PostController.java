package com.example.fakebookproject.api.post.controller;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.api.post.service.PostService;
import com.example.fakebookproject.common.dto.PageResponse;
import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * @param loginId    현재 로그인 된 사용자 정보
     * @param requestDto 등록할 게시글 정보
     * @return 200 OK
     */
    @PostMapping
    public ResponseEntity<Void> createPost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestBody @Valid PostCreateRequestDto requestDto
    ) {
        postService.createPost(requestDto, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<PageResponse<PostResponseDto>> findMyPost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<PostResponseDto> responseDto = postService.findMyPost(loginId, page, size);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostResponseDto>> findRelatedPost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<PostResponseDto> responseDto = postService.findRelatedPost(loginId, page, size);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findPostById(
            @PathVariable Long id
    ) {
        PostResponseDto responseDto = postService.findPostById(id);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestBody @Valid PostUpdateDto requestDto,
            @PathVariable Long id
    ) {
        postService.updatePost(id, loginId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @SessionAttribute("loginUser") Long loginId,
            @PathVariable Long id
    ) {
        postService.deletePost(id, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
