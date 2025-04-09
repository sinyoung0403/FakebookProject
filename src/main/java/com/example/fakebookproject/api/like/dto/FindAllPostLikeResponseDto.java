package com.example.fakebookproject.api.like.dto;

import com.example.fakebookproject.api.like.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllPostLikeResponseDto {
    private final String userName;
    private final String imageUrl;

    public FindAllPostLikeResponseDto(PostLike postLike) {
        this.userName = postLike.getUser().getUserName();
        this.imageUrl = postLike.getUser().getImageUrl();
    }

    /**
     * PostLike 를 ResponseDto 로 변환해주는 정적 메서드
     * @param postLike
     * @returnd
     */
    public static FindAllPostLikeResponseDto from(PostLike postLike) {
        return new FindAllPostLikeResponseDto(postLike.getUser().getUserName(), postLike.getUser().getImageUrl());
    }
}
