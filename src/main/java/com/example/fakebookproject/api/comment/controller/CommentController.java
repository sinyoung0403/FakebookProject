package com.example.fakebookproject.api.comment.controller;

import com.example.fakebookproject.api.comment.dto.CommentCreateRequestDto;
import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.api.comment.dto.CommentUpdateRequestDto;
import com.example.fakebookproject.api.comment.service.CommentService;
import com.example.fakebookproject.common.dto.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
@Validated
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     *
     * @param postId 게시글 식별자
     * @param requestDto 등록할 댓글 정보 (댓글 내용)
     * @param HttpRequest 횬재 로그인된 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @NotNull @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            HttpServletRequest HttpRequest
    ) {
        Long loginUserId = (Long) HttpRequest.getAttribute("userId");
        CommentResponseDto response = commentService.createComment(loginUserId, postId, requestDto.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 목록 조회
     *
     * @param postId 게시글 식별자
     * @param pageable 페이징 정보
     * @return 조회된 댓글 목록 (페이징 처리 포함)
     */
    @GetMapping
    public ResponseEntity<PageResponse<CommentResponseDto>> findAllComments(
            @NotNull @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<CommentResponseDto> response = commentService.findAllComments(postId, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 수정
     *
     * @param commentId 댓글 식별자
     * @param requestDto 수정할 댓글 정보 (댓글 내용)
     * @param HttpRequest 현재 로그인된 사용자 정보
     * @return 수정된 댓글 정보
     */
    @PatchMapping ("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @NotNull @PathVariable Long postId,
            @NotNull @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            HttpServletRequest HttpRequest
    ) {
        Long loginUserId = (Long) HttpRequest.getAttribute("userId");
        CommentResponseDto response = commentService.updateComment(loginUserId, postId, commentId, requestDto.getContent());

        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 식별자
     * @param HttpRequest 현재 로그인된 사용자 정보
     * @return 성공 시 204 No Content 응답
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @NotNull @PathVariable Long postId,
            @NotNull @PathVariable Long commentId,
            HttpServletRequest HttpRequest
    ) {
        Long loginUserId = (Long) HttpRequest.getAttribute("userId");
        commentService.deleteComment(loginUserId, postId, commentId);

        return ResponseEntity.noContent().build();
    }
}