package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendStatusResponseDto;
import com.example.fakebookproject.common.dto.PageResponse;

public interface FriendService {

    FriendStatusResponseDto requestFriend(Long requestUserId, Long responseUserId);

    PageResponse<FriendResponseDto> findMyFriends(Long loginUserId, int page, int size);

    PageResponse<FriendResponseDto> recommendFriends(Long loginUserId, int page, int size);

    PageResponse<FriendResponseDto> receivedFriends(Long loginUserId, int page, int size);

    PageResponse<FriendResponseDto> sentFriends(Long loginUserId, int page, int size);

    FriendStatusResponseDto responseFriend(Long loginUserId, Long requestUserId);

    void deleteFriend(Long loginUserId, Long requestUserId);
}
