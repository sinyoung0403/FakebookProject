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
}