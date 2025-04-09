package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendStatusResponseDto;
import com.example.fakebookproject.common.dto.PageResponse;
import org.springframework.data.domain.Page;


public interface FriendService {

    FriendStatusResponseDto requestFriend(Long requestUserId, Long responseUserId);

    PageResponse<FriendResponseDto> findMyFriends(Long userId, int page, int size);

    PageResponse<FriendResponseDto> recommendFriends(Long userId, int page, int size);

    PageResponse<FriendResponseDto> receivedFriends(Long userId, int page, int size);

    PageResponse<FriendResponseDto> sentFriends(Long userId, int page, int size);

    void responseFriend(Long userId, Long id, int status);

    void deleteFriend(Long userId, Long id);
}
