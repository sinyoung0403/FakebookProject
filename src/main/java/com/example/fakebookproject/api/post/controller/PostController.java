package com.example.fakebookproject.api.post.controller;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.api.post.service.PostService;
import com.example.fakebookproject.common.dto.PageResponse;
import com.example.fakebookproject.common.util.TokenUtils;
import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TokenUtils tokenUtils;

    /**
     * 게시글을 생성
     *
     * @param requestDto    등록할 게시글 정보
     * @return              성공 시 CREATED
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            HttpServletRequest request,
            @RequestBody @Valid PostCreateRequestDto requestDto
    ) {
        String token = request.getHeader("ACCESS_TOKEN");
        Long userId = tokenUtils.getUserIdFromAccessToken(token);

        PostResponseDto responseDto = postService.createPost(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable

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
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<PostResponseDto> responseDto = postService.findRelatedPost(loginId, pageable);

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
     * @param loginId       현재 로그인 된 사용자 정보
     * @param requestDto    수정할 내용 정보
     * @param id            수정하려는 게시글 식별자
     * @return              성공시 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @SessionAttribute("loginUser") Long loginId,
            @RequestBody @Valid PostUpdateDto requestDto,
            @PathVariable Long id
    ) {
        PostResponseDto responseDto = postService.updatePost(id, loginId, requestDto);

        return ResponseEntity.ok().body(responseDto);
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
