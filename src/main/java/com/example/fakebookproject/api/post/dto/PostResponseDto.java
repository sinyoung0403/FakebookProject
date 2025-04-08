package com.example.fakebookproject.api.post.dto;

import com.example.fakebookproject.api.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    private final Long id;
    private final String contents;
    private final String imageUrl;
    private final LocalDateTime updatedAt;
    private final Long likeCount;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.contents = post.getContents();
        this.imageUrl = post.getImageUrl();
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikeCount();
    }
}
