package com.example.fakebookproject.api.like.dto;

import com.example.fakebookproject.api.like.entity.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllCommentLikeResponseDto {
    private final String userName;
    private final String imageUrl;

    public FindAllCommentLikeResponseDto(CommentLike commentLike) {
        this.userName = commentLike.getUser().getUserName();
        this.imageUrl = commentLike.getUser().getImageUrl();
    }

    /**
     * Comment Like 를 ResponseDto 로 변환해주는 정적 메서드
     *
     * @param commentLike
     * @return
     */
    public static FindAllCommentLikeResponseDto from(CommentLike commentLike) {
        return new FindAllCommentLikeResponseDto(commentLike.getUser().getImageUrl(), commentLike.getUser().getUserName());
    }
}