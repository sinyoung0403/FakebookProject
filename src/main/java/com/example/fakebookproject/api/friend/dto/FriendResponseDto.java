package com.example.fakebookproject.api.friend.dto;

import lombok.Getter;

/**
 * 목록 조회 DTO
 */
@Getter
public class FriendResponseDto {

    /**
     * 친구 id(pk)
     */
    private Long id;

    /**
     * 친구 이름
     */
    private String userName;

    /**
     * 친구 이미지
     */
    private String imageUrl;

    public FriendResponseDto(Long id, String userName, String imageUrl) {
        this.id = id;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

}
