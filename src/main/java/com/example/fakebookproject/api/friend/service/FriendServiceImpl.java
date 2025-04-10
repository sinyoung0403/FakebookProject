package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendStatusResponseDto;
import com.example.fakebookproject.api.friend.entity.FriendStatus;
import com.example.fakebookproject.api.friend.repository.FriendRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.dto.PageResponse;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public FriendStatusResponseDto requestFriend(Long requestUserId, Long responseUserId) {

        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        User responseUser = userRepository.findById(responseUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        //같은 친구에게 다시 한 번 친구 요청 할 경우, "이미 친구 신청한 회원입니다." 에러
        Optional<FriendStatus> isExist = friendRepository.findFriendStatusByRequestUserIdAndResponseUserIdOrResponseUserIdAndRequestUserId(requestUser.getId(), responseUser.getId());
        if(isExist.isPresent()) {
            throw new CustomException(ExceptionCode.ALREADY_REQUESTED);
        }

        FriendStatus friendStatus =new FriendStatus();
        friendStatus.setUser(requestUser, responseUser);

        friendRepository.save(friendStatus);

        return new FriendStatusResponseDto(
                friendStatus.getRequestUser().getId(),
                friendStatus.getResponseUser().getId(),
                friendStatus.getStatus()
        );
    }

    /**
     * 내 친구 목록 조회
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResponse<FriendResponseDto> findMyFriends(Long loginUserId, int page, int size) {

        List<Long> friendIds = friendRepository.findAllByUserIdAndStatusAcceptedOrElseThrow(loginUserId);

        Page<User> friendPage = userRepository.findByIdIn(friendIds, PageRequest.of(page,size));

        Page<FriendResponseDto> friendResponseDtoPage = friendPage.map(friend -> new FriendResponseDto(friend.getId(), friend.getUserName(), friend.getImageUrl()));

        return new PageResponse<>(
                friendResponseDtoPage.getContent(),
                friendResponseDtoPage.getNumber(),
                friendResponseDtoPage.getSize(),
                friendResponseDtoPage.getTotalElements(),
                friendResponseDtoPage.getTotalPages()
        );
    }

    /**
     * 추천 친구 목록 조회
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResponse<FriendResponseDto> recommendFriends(Long loginUserId, int page, int size) {

        User findUser = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        List<Long> recommendationList = friendRepository.findRecommendationAllByUserIdOrElseThrow(findUser.getId(), findUser.getCityName(), findUser.getHobby());

        Page<User> recommendationPage = userRepository.findByIdIn(recommendationList, PageRequest.of(page,size));

        Page<FriendResponseDto> friendResponseDtoPage = recommendationPage.map(friend -> new FriendResponseDto(friend.getId(), friend.getUserName(), friend.getImageUrl()));

        return new PageResponse<>(
                friendResponseDtoPage.getContent(),
                friendResponseDtoPage.getNumber(),
                friendResponseDtoPage.getSize(),
                friendResponseDtoPage.getTotalElements(),
                friendResponseDtoPage.getTotalPages()
        );
    }

    /**
     * 내가 요청 받은 친구 목록
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResponse<FriendResponseDto> receivedFriends(Long loginUserId, int page, int size) {

        List<Long> receivedList = friendRepository.findReceivedAllByUserIdOrElseThrow(loginUserId);

        Page<User> receivedPage = userRepository.findByIdIn(receivedList, PageRequest.of(page,size));

        Page<FriendResponseDto> friendResponseDtoPage = receivedPage.map(friend -> new FriendResponseDto(friend.getId(), friend.getUserName(), friend.getImageUrl()));

        return new PageResponse<>(
                friendResponseDtoPage.getContent(),
                friendResponseDtoPage.getNumber(),
                friendResponseDtoPage.getSize(),
                friendResponseDtoPage.getTotalElements(),
                friendResponseDtoPage.getTotalPages()
        );
    }

    /**
     * 내가 요청한 친구 목록
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResponse<FriendResponseDto> sentFriends(Long loginUserId, int page, int size) {

        List<Long> sentList = friendRepository.findSentAllByUserIdOrElseThrow(loginUserId);

        Page<User> sentPage = userRepository.findByIdIn(sentList, PageRequest.of(page,size));

        Page<FriendResponseDto> friendResponseDtoPage = sentPage.map(friend -> new FriendResponseDto(friend.getId(), friend.getUserName(), friend.getImageUrl()));

        return new PageResponse<>(
                friendResponseDtoPage.getContent(),
                friendResponseDtoPage.getNumber(),
                friendResponseDtoPage.getSize(),
                friendResponseDtoPage.getTotalElements(),
                friendResponseDtoPage.getTotalPages()
        );
    }

    /**
     * 친구 요청 수락
     * @param loginUserId
     * @param requestUserId
     */
    @Override
    @Transactional
    public FriendStatusResponseDto responseFriend(Long loginUserId, Long requestUserId) {
        FriendStatus friendStatus = friendRepository.findByRequestUserIdAndResponseUserIdOrElseThrow(requestUserId, loginUserId);
        friendStatus.update(1);

        return new FriendStatusResponseDto(
                friendStatus.getRequestUser().getId(),
                friendStatus.getResponseUser().getId(),
                friendStatus.getStatus()
        );
    }

    /**
     * 친구 요청 거절
     * @param loginUserId
     * @param requestUserId
     */
    @Override
    @Transactional
    public void deleteFriend(Long loginUserId, Long requestUserId) {
        FriendStatus friendStatus = friendRepository.findByRequestUserIdAndResponseUserIdOrElseThrow(requestUserId, loginUserId);
        friendRepository.deleteByRequestUserIdAndResponseUserId(requestUserId, loginUserId);
    }

}