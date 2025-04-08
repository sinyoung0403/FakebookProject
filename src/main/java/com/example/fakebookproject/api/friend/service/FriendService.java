package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;

public interface FriendService {

    FriendResponseDto requestFriend(Long requestUserId, Long responseUserId);
}
