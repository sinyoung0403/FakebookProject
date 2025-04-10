package com.example.fakebookproject.api.friend.controller;

import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendStatusResponseDto;
import com.example.fakebookproject.api.friend.service.FriendService;
import com.example.fakebookproject.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 요청
     * @param requestUserId
     * @param responseUserId
     * @return
     */
    @PostMapping("/requests")
    public ResponseEntity<FriendStatusResponseDto> requestFriend(
            @SessionAttribute ("loginUser") Long requestUserId,
            @RequestParam Long responseUserId
    ) {
        FriendStatusResponseDto friendResponseDto = friendService.requestFriend(requestUserId, responseUserId);
        return new ResponseEntity<>(friendResponseDto, HttpStatus.OK);
    }

    /**
     * 내 친구 목록 조회
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<PageResponse<FriendResponseDto>> findMyFriends(
            @SessionAttribute ("loginUser") Long loginUserId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        PageResponse<FriendResponseDto> friendResponseDtoList = friendService.findMyFriends(loginUserId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    /**
     * 추천 친구 목록 조회
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/recommendations")
    public ResponseEntity<PageResponse<FriendResponseDto>> recommendFriends(
            @SessionAttribute ("loginUser") Long loginUserId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        PageResponse<FriendResponseDto> friendResponseDtoList = friendService.recommendFriends(loginUserId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    /**
     * 나한테 요청 받은 친구 목록
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/received")
    public ResponseEntity<PageResponse<FriendResponseDto>> receivedFriends(
            @SessionAttribute ("loginUser") Long loginUserId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        PageResponse<FriendResponseDto> friendResponseDtoList = friendService.receivedFriends(loginUserId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    /**
     * 내가 요청한 친구 목록
     * @param loginUserId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/sent")
    public ResponseEntity<PageResponse<FriendResponseDto>> sentFriends(
            @SessionAttribute ("loginUser") Long loginUserId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        PageResponse<FriendResponseDto> friendResponseDtoList = friendService.sentFriends(loginUserId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    /**
     * 친구 요청 수락
     * @param loginUserId
     * @param requestUserId
     * @return
     */
    @PatchMapping("/responses/{requestUserId}")
    public ResponseEntity<FriendStatusResponseDto> responseFriend(
            @SessionAttribute ("loginUser") Long loginUserId,
            @PathVariable Long requestUserId
    ) {
        FriendStatusResponseDto friendResponseDto = friendService.responseFriend(loginUserId, requestUserId);
        return new ResponseEntity<>(friendResponseDto, HttpStatus.OK);
    }

    /**
     * 친구 요청 거절
     * @param loginUserId
     * @param requestUserId
     * @return
     */
    @DeleteMapping("/{requestUserId}")
    public ResponseEntity<Void> deleteFriend(
            @SessionAttribute ("loginUser") Long loginUserId,
            @PathVariable Long requestUserId
    ) {
        friendService.deleteFriend(loginUserId, requestUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}