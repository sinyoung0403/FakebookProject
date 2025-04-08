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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

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
//
//    @Override
//    public List<FriendPageResponseDto> findMyFriends(Long userId, int page, int size) {
//        //Pageable 객체 생성
//        Pageable pageable = PageRequest.of(page, size);
//
//        List<Object[]> friendsList = friendRepository.findAllByUserIdAndStatusAcceptedOrElseThrow(userId);
//
//        //모든 친구
//        //long totalCount = friendsList.getTotalElements();
//
//        return friendsList
//                .stream()
//                .map(row -> {
//                    FriendStatus friend = (FriendStatus) row[0];
//                    User user = (User) row[1];
//                    return new FriendPageResponseDto(
//                            user.getUserName(),
//                            user.getImageUrl()
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<FriendPageResponseDto> recommendFriends(Long userId, int page, int size) {
//        //Pageable 객체 생성
//        Pageable pageable = PageRequest.of(page, size);
//
//        //Page<Object[]> recommendationList = friendRepository.findRecommendationAllByUserIdOrElseThrow(userId, pageable);
//
//        List<Object[]> recommendationList = friendRepository.findRecommendationAllByUserIdOrElseThrow(userId);
//
//        return recommendationList
//                .stream()
//                .map(row -> {
//                    FriendStatus friend = (FriendStatus) row[0];
//                    User user = (User) row[1];
//                    return new FriendPageResponseDto(
//                            user.getUserName(),
//                            user.getImageUrl()
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<FriendPageResponseDto> receivedFriends(Long userId, int page, int size) {
//        //Pageable 객체 생성
//        Pageable pageable = PageRequest.of(page, size);
//
//        //Page객체로 데이터 생성
//        List<Object[]> receivedList = friendRepository.findReceivedAllByUserIdOrElseThrow(userId);
//
//        //모든 추천 친구
//        //long totalCount = receivedList.getTotalElements();
//
//        return receivedList
//                .stream()
//                .map(row -> {
//                    FriendStatus friend = (FriendStatus) row[0];
//                    User user = (User) row[1];
//                    return new FriendPageResponseDto(
//                            user.getUserName(),
//                            user.getImageUrl()
//                    );
//                })
//                .collect(Collectors.toList());
//
//    }





}
