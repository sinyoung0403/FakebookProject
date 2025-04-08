package com.example.fakebookproject.api.comment.dto;

import com.example.fakebookproject.api.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO
 */
@Getter
@AllArgsConstructor
public class CommentResponseDto {

    /**
     * 댓글 식별자
     */
    private final Long id;

    /**
     * 게시글 식별자
     */
    private final Long postId;

    /**
     * 사용자 식별자
     */
    private final Long userId;

    /**
     * 사용자 이름
     */
    private final String userName;

    /**
     * 댓글 내용
     */
    private final String content;

    /**
     * 댓글 좋아요 수
     */
    private Long likeCount;

    /**
     * 댓글 등록일시
     */
    private final LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
        this.userName = comment.getUser().getUserName();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
    }
}
