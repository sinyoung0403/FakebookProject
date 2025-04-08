package com.example.fakebookproject.api.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 수정 요청 DTO
 */
@Getter
@AllArgsConstructor
public class CommentUpdateRequestDto {

    /**
     * 댓글 내용 (최대 1000자)
     */
    @Size(max = 1000, message = "1000자를 초과할 수 없습니다.")
    private final String content;
}
