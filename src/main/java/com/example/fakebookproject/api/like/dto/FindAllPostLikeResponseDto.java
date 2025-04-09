package com.example.fakebookproject.api.like.dto;

import com.example.fakebookproject.api.like.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllPostLikeResponseDto {
    private final Long userId;
    private final String userName;
    private final String imageUrl;

    public FindAllPostLikeResponseDto(PostLike postLike) {
        this.userId = postLike.getUser().getId();
        this.userName = postLike.getUser().getUserName();
        this.imageUrl = postLike.getUser().getImageUrl();
    }
}
