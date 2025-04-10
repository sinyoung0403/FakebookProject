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
     * @param loginId       현재 로그인 된 사용자 정보
     * @param requestDto    등록할 게시글 정보
     * @return              성공 시 CREATED
     */
    @PostMapping
    public ResponseEntity<Void> createPost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestBody @Valid PostCreateRequestDto requestDto
    ) {
        postService.createPost(requestDto, loginId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 내 게시글 찾기
     *
     * @param loginId   현재 로그인 된 사용자 정보
     * @param pageable  페이징 정보
     * @return          조회된 게시글 목록 (본인)
     */
    @GetMapping("/me")
    public ResponseEntity<PageResponse<PostResponseDto>> findMyPost(
            @SessionAttribute("loginUser") Long loginId,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable

    ) {
        PageResponse<PostResponseDto> responseDto = postService.findMyPost(loginId, pageable);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 나와 친구의 게시글 찾기
     *
     * @param loginId   현재 로그인 된 사용자 정보
     * @param pageable  페이징 정보
     * @return          조회된 게시글 목록 (본인 + 친구)
     */
    @GetMapping
    public ResponseEntity<PageResponse<PostResponseDto>> findRelatedPost(
            @SessionAttribute("loginUser") Long loginId,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<PostResponseDto> responseDto = postService.findRelatedPost(loginId, pageable);

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
     * @param loginId       현재 로그인 된 사용자 정보
     * @param requestDto    수정할 내용 정보
     * @param id            수정하려는 게시글 식별자
     * @return              성공시 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestBody @Valid PostUpdateDto requestDto,
            @PathVariable Long id
    ) {
        postService.updatePost(id, loginId, requestDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     *
     * @param loginId       현재 로그인 된 사용자 정보
     * @param id            삭제하려는 게시글 식별자
     * @return              성공시 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @SessionAttribute("loginUser") Long loginId,
            @PathVariable Long id
    ) {
        postService.deletePost(id, loginId);

        return ResponseEntity.ok().build();
    }


}
