package com.example.fakebookproject.api.like.controller;

import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.like.service.CommentLikeService;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{post_id}/comments/{commentId}/likes")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<String> createCommentLike(
            @PathVariable Long post_id,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        commentLikeService.createCommentLike(post_id, commentId, loginUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요가 추가되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<FindAllCommentLikeResponseDto>> findAllCommentLike(
            @PathVariable Long post_id,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        List<FindAllCommentLikeResponseDto> list = commentLikeService.findAllCommentLike(post_id, commentId, loginUserId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCommentLike(
            @PathVariable Long post_id,
            @PathVariable Long commentId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        commentLikeService.deleteCommentLike(post_id, commentId, loginUserId);
        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }
}
