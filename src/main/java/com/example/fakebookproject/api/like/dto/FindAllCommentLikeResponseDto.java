package com.example.fakebookproject.api.like.dto;

import com.example.fakebookproject.api.like.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllCommentLikeResponseDto {
    private String userName;
    private String imageUrl;

    /**
     * Comment Like 를 ResponseDto 로 변환해주는 정적 메서드
     * @param postLike
     * @return
     */
    public static FindAllCommentLikeResponseDto from(PostLike postLike) {
        return new FindAllCommentLikeResponseDto(postLike.getUser().getImageUrl(), postLike.getUser().getUserName());
    }
}