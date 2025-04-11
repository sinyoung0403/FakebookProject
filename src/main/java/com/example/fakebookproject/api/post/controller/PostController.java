package com.example.fakebookproject.api.post.controller;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.api.post.service.PostService;
import com.example.fakebookproject.common.dto.PageResponse;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 게시글을 생성
     *
     * @param request       현재 로그인 된 사용자 정보
     * @param requestDto    등록할 게시글 정보
     * @return              성공 시 CREATED
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            HttpServletRequest request,
            @RequestBody @Valid PostCreateRequestDto requestDto
    ) {
        Long userId = (Long) request.getAttribute("userId");

        PostResponseDto responseDto = postService.createPost(requestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 내 게시글 찾기
     *
     * @param request   현재 로그인 된 사용자 정보
     * @param pageable  페이징 정보
     * @return          조회된 게시글 목록 (본인)
     */
    @GetMapping("/me")
    public ResponseEntity<PageResponse<PostResponseDto>> findMyPost(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable

    ) {
        Long userId = (Long) request.getAttribute("userId");

        PageResponse<PostResponseDto> responseDto = postService.findMyPost(userId, pageable);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 나와 친구의 게시글 찾기
     *
     * @param request   현재 로그인 된 사용자 정보
     * @param pageable  페이징 정보
     * @return          조회된 게시글 목록 (본인 + 친구)
     */
    @GetMapping
    public ResponseEntity<PageResponse<PostResponseDto>> findRelatedPost(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        Long userId = (Long) request.getAttribute("userId");

        PageResponse<PostResponseDto> responseDto = postService.findRelatedPost(userId, pageable);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * user_id로 게시물  찾기
     *
     * @param id    유저 식별자
     * @return      해당 식별자로 조회된 게시글
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<PageResponse<PostResponseDto>> findPostByUserId(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<PostResponseDto> responseDto = postService.findPostByUserId(id, pageable);

        return ResponseEntity.ok(responseDto);
    }


    /**
     * post_id로 게시물  찾기
     *
     * @param id    게시글 식별자
     * @return      해당 식별자로 조회된 게시글
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findPostById(
            @PathVariable Long id
    ) {
        PostResponseDto responseDto = postService.findPostById(id);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 게시글 수정
     *
     * @param request       현재 로그인 된 사용자 정보
     * @param requestDto    수정할 내용 정보
     * @param id            수정하려는 게시글 식별자
     * @return              성공시 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            HttpServletRequest request,
            @RequestBody @Valid PostUpdateDto requestDto,
            @PathVariable Long id
    ) {
        Long userId = (Long) request.getAttribute("userId");

        if(requestDto.getContents() == null && requestDto.getImageUrl() == null){
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        PostResponseDto responseDto = postService.updatePost(id, userId, requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 게시글 삭제
     *
     * @param request       현재 로그인 된 사용자 정보
     * @param id            삭제하려는 게시글 식별자
     * @return              성공시 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long userId = (Long) request.getAttribute("userId");

        postService.deletePost(id, userId);

        return ResponseEntity.ok().build();
    }


}
