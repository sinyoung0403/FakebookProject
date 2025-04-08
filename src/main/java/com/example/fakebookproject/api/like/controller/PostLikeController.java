package com.example.fakebookproject.api.like.controller;

import com.example.fakebookproject.api.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping
  public ResponseEntity<String> createPostLike(
          @PathVariable Long postId
          //, @SessionAttribute(name = "loginUser") LoginDto loginDto
  ) {
    postLikeService.createPostLike(postId);
    return ResponseEntity.status(HttpStatus.CREATED).body("좋아요를 추가했습니다.");
  }
}
