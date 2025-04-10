package com.example.fakebookproject.api.friend.dto;

import lombok.Getter;

/**
 * 친구 상태 DTO
 */
@Getter
public class FriendStatusResponseDto {

    /**
     * 요청 id
     */
    private final Long requestUserId;

    /**
     * 응답 id
     */
    private final Long responseUserId;

    /**
     * 상태
     */
    private final int status;

    public FriendStatusResponseDto(Long requestUserId, Long responseUserId, int status) {
        this.requestUserId = requestUserId;
        this.responseUserId = responseUserId;
        this.status = status;
    }
}
