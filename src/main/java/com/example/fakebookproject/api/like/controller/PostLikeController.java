package com.example.fakebookproject.api.like.controller;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;
import com.example.fakebookproject.api.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * Post 에 Like 추가
     *
     * @param postId
     * @return String : 성공 여부 | 201 CREATED
     */
    @PostMapping
    public ResponseEntity<String> createPostLike(
            @PathVariable Long postId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        postLikeService.createPostLike(postId, loginUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요를 추가했습니다.");
    }

    /**
     * Post 에 추가된 Like 모두 조회
     *
     * @param postId
     * @return 좋아요를 누른 사용자 (사용자 이름, 이미지 URL) List | 200 OK
     */
    @GetMapping
    public ResponseEntity<List<FindAllPostLikeResponseDto>> findAllPostLike(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postLikeService.findAllPostLike(postId));
    }


    /**
     * Post 에 추가된 Like 삭제
     *
     * @param postId
     * @return String : 성공 여부 | 204 NO_CONTENT
     */
    @DeleteMapping
    public ResponseEntity<String> deleteLike(
            @PathVariable Long postId,
            @SessionAttribute("loginUser") Long loginUserId
    ) {
        postLikeService.deletePostLike(postId, loginUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("좋아요가 취소되었습니다.");
    }
}
