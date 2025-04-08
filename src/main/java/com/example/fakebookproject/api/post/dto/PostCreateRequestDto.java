package com.example.fakebookproject.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PostCreateRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private final String contents;

    private final String imageUrl;
}
