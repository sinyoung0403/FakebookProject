package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendPageResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.entity.FriendStatus;
import com.example.fakebookproject.api.friend.repository.FriendRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    /**
     * 친구 요청
     * @param requestUserId
     * @param responseUserId
     * @return
     */
    @Override
    public FriendResponseDto requestFriend(Long requestUserId, Long responseUserId) {

        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        User responseUser = userRepository.findById(responseUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        FriendStatus friendStatus =new FriendStatus();
        friendStatus.setUser(requestUser, responseUser);

        friendRepository.save(friendStatus);

        return new FriendResponseDto(
                friendStatus.getId(),
                friendStatus.getStatus()
        );
    }

    /**
     * 내 친구 목록 조회
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<FriendPageResponseDto> findMyFriends(Long userId, int page, int size) {

        List<Long> friendIds = friendRepository.findAllByUserIdAndStatusAcceptedOrElseThrow(userId);

        Page<User> friendPage = userRepository.findByIdIn(friendIds, PageRequest.of(page,size));

        return friendPage.map(user -> new FriendPageResponseDto(user.getUserName(), user.getImageUrl()));
    }

    /**
     * 추천 친구 목록 조회
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<FriendPageResponseDto> recommendFriends(Long userId, int page, int size) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        List<Long> recommendationList = friendRepository.findRecommendationAllByUserIdOrElseThrow(userId, findUser.getCityName());

        Page<User> recommendationPage = userRepository.findByIdIn(recommendationList, PageRequest.of(page,size));

        return recommendationPage.map(user -> new FriendPageResponseDto(user.getUserName(), user.getImageUrl()));
    }

    /**
     * 나한테 요청 받은 친구 목록
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<FriendPageResponseDto> receivedFriends(Long userId, int page, int size) {

        List<Long> receivedList = friendRepository.findReceivedAllByUserIdOrElseThrow(userId);

        Page<User> receivedPage = userRepository.findByIdIn(receivedList, PageRequest.of(page,size));

        return receivedPage.map(user -> new FriendPageResponseDto(user.getUserName(), user.getImageUrl()));
    }

    /**
     * 내가 요청한 친구 목록
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<FriendPageResponseDto> sentFriends(Long userId, int page, int size) {

        List<Long> sentList = friendRepository.findSentAllByUserIdOrElseThrow(userId);

        Page<User> sentPage = userRepository.findByIdIn(sentList, PageRequest.of(page,size));

        return sentPage.map(user -> new FriendPageResponseDto(user.getUserName(), user.getImageUrl()));
    }

    /**
     * 친구 요청 수락
     * @param userId
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public void responseFriend(Long userId, Long id, int status) {

        FriendStatus friendStatus = friendRepository.findFriendStatusByIdOrElseThrow(id);

        if (!friendStatus.getResponseUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        friendStatus.update(status);

    }

    /**
     * 친구 요청 거절
     * @param userId
     * @param id
     */
    @Override
    @Transactional
    public void deleteFriend(Long userId, Long id) {

        FriendStatus friendStatus = friendRepository.findFriendStatusByIdOrElseThrow(id);

        if (!friendStatus.getResponseUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        friendRepository.deleteById(id);

    }





}
