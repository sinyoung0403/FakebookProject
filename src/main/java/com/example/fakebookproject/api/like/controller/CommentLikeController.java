package com.example.fakebookproject.api.like.controller;

import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.like.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments/{commentId}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    /**
     * Comment 에 Like 추가
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @return String : 성공 여부 | 201 CREATED
     */
    @PostMapping
    public ResponseEntity<String> createCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        commentLikeService.createCommentLike(postId, commentId, loginUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요가 추가되었습니다.");
    }

    /**
     * Comment 에 추가된 Like 모두 조회
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @return 좋아요를 누른 사용자 (사용자 이름, 이미지 URL) List | 200 OK
     */
    @GetMapping
    public ResponseEntity<List<FindAllCommentLikeResponseDto>> findAllCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        List<FindAllCommentLikeResponseDto> list = commentLikeService.findAllCommentLike(postId, commentId, loginUserId);
        return ResponseEntity.ok(list);
    }

    /**
     * Comment 에 추가된 Like 삭제
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @return String : 성공 여부 | 204 NO_CONTENT
     */
    @DeleteMapping
    public ResponseEntity<String> deleteCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        commentLikeService.deleteCommentLike(postId, commentId, loginUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("좋아요가 취소되었습니다.");
    }
}
