package com.example.fakebookproject.api.friend.controller;

import com.example.fakebookproject.api.friend.dto.FriendPageResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/requests")
    public ResponseEntity<FriendResponseDto> requestFriend(
            HttpServletRequest request,
            @RequestParam Long requestUserId,
            @RequestParam Long responseUserId
    ) {
        FriendResponseDto friendResponseDto = friendService.requestFriend(requestUserId, responseUserId);
        return new ResponseEntity<>(friendResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<FriendPageResponseDto>> findMyFriends(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Page<FriendPageResponseDto> friendResponseDtoList = friendService.findMyFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<Page<FriendPageResponseDto>> recommendFriends(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Page<FriendPageResponseDto> friendResponseDtoList = friendService.recommendFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<Page<FriendPageResponseDto>> receivedFriends(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Page<FriendPageResponseDto> friendResponseDtoList = friendService.receivedFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/sent")
    public ResponseEntity<Page<FriendPageResponseDto>> sentFriends(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Page<FriendPageResponseDto> friendResponseDtoList = friendService.sentFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @PatchMapping("/responses/{id}")
    public ResponseEntity<Void> responseFriend(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam int status) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        friendService.responseFriend(userId, id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriend(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        friendService.deleteFriend(userId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
