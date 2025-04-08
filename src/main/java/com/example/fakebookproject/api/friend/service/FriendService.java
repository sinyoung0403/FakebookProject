package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendPageResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.repository.FriendRepository;

import java.util.List;

public interface FriendService {

    FriendResponseDto requestFriend(Long requestUserId, Long responseUserId);

//    List<FriendPageResponseDto> findMyFriends(Long userId, int page, int size);
//
//    List<FriendPageResponseDto> recommendFriends(Long userId, int page, int size);
//
//    List<FriendPageResponseDto> receivedFriends(Long userId, int page, int size);
}
