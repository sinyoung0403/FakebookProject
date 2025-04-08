package com.example.fakebookproject.api.friend.dto;

import lombok.Getter;

@Getter
public class FriendPageResponseDto {

    private String userName;
    private String imageUrl;

    public FriendPageResponseDto(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

}
