package com.example.fakebookproject.api.like.dto;

import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class FindAllPostLikeResponseDto {
    private String userName;
    private String imageUrl;

    /**
     * PostLike 를 ResponseDto 로 변환해주는 정적 메서드
     * @param postLike
     * @return
     */
    public static FindAllPostLikeResponseDto from(PostLike postLike) {
        return new FindAllPostLikeResponseDto(postLike.getUser().getUserName(), postLike.getUser().getImageUrl());
    }
}
