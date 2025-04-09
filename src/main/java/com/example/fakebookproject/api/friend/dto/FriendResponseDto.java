package com.example.fakebookproject.api.friend.dto;

import lombok.Getter;

/**
 * 목록 조회 DTO
 */
@Getter
public class FriendResponseDto {

    /**
     * 친구 이름
     */
    private String userName;

    /**
     * 친구 이미지
     */
    private String imageUrl;

    public FriendResponseDto(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

}
