package com.example.fakebookproject.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateDto {
    private final String contents;

    private final String imageUrl;
}
