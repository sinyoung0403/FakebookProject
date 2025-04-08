package com.example.fakebookproject.api.comment.controller;

import com.example.fakebookproject.api.comment.dto.CommentCreateRequestDto;
import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.api.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     *
     * @param postId 게시글 식별자
     * @param requestDto 등록할 댓글 정보 (댓글 내용)
     * @param httpRequest 횬재 로그인된 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@NotNull @PathVariable Long postId,
                                                            @Valid @RequestBody CommentCreateRequestDto requestDto,
                                                            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        CommentResponseDto response = commentService.createComment(userId, postId, requestDto.getContent());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 댓글 목록 조회
     *
     * @param postId 게시글 식별자
     * @param httpRequest 현재 로그인된 사용자 정보
     * @param pageable 페이징 정보
     * @return 조회된 댓글 목록 (페이징 처리 포함)
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> findAllComments(
            @NotNull @PathVariable Long postId,
            HttpServletRequest httpRequest,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        Page<CommentResponseDto> response = commentService.findAllComments(userId, postId, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
