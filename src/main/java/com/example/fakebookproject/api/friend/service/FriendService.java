package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendPageResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import org.springframework.data.domain.Page;


public interface FriendService {

    FriendResponseDto requestFriend(Long requestUserId, Long responseUserId);

    Page<FriendPageResponseDto> findMyFriends(Long userId, int page, int size);

    Page<FriendPageResponseDto> recommendFriends(Long userId, int page, int size);

    Page<FriendPageResponseDto> receivedFriends(Long userId, int page, int size);

    Page<FriendPageResponseDto> sentFriends(Long userId, int page, int size);

    void responseFriend(Long userId, Long id, int status);

    void deleteFriend(Long userId, Long id);
}
