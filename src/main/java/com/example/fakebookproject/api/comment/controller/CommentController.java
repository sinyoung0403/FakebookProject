package com.example.fakebookproject.api.comment.controller;

import com.example.fakebookproject.api.comment.dto.CommentCreateRequestDto;
import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.api.comment.dto.CommentUpdateRequestDto;
import com.example.fakebookproject.api.comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
     * @param loginUserId 횬재 로그인된 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @NotNull @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        CommentResponseDto response = commentService.createComment(loginUserId, postId, requestDto.getContent());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 댓글 목록 조회
     *
     * @param postId 게시글 식별자
     * @param pageable 페이징 정보
     * @return 조회된 댓글 목록 (페이징 처리 포함)
     */
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> findAllComments(
            @NotNull @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        Page<CommentResponseDto> response = commentService.findAllComments(postId, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     *
     * @param commentId 댓글 식별자
     * @param requestDto 수정할 댓글 정보 (댓글 내용)
     * @param loginUserId 현재 로그인된 사용자 정보
     * @return 수정된 댓글 정보
     */
    @PostMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @NotNull @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        CommentResponseDto response = commentService.updateComment(loginUserId, commentId, requestDto.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 식별자
     * @param loginUserId 현재 로그인된 사용자 식별자
     * @return 성공 시 204 No Content 응답
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @NotNull @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        commentService.deleteComment(loginUserId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
