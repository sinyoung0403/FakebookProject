package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.entity.FriendStatus;
import com.example.fakebookproject.api.friend.repository.FriendRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    //private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendResponseDto requestFriend(Long requestUserId, Long responseUserId) {
        //User user = UserRepository.findById(id);

        FriendStatus friendStatus =new FriendStatus();
        //friendStatus.setUser(user.getId());

        friendRepository.save(friendStatus);

        return new FriendResponseDto(
                friendStatus.getId(),
                friendStatus.getStatus()
        );


    }
}
