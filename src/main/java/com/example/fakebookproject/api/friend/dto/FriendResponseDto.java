package com.example.fakebookproject.api.friend.dto;

import lombok.Getter;

@Getter
public class FriendResponseDto {

    private final Long id;
    private final int status;

    public FriendResponseDto(Long id, int status) {
        this.id = id;
        this.status = status;
    }
}
