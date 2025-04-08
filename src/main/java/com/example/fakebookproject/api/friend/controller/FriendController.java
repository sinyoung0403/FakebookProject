package com.example.fakebookproject.api.friend.controller;

import com.example.fakebookproject.api.friend.dto.FriendPageResponseDto;
import com.example.fakebookproject.api.friend.dto.FriendResponseDto;
import com.example.fakebookproject.api.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/requests")
    public ResponseEntity<FriendResponseDto> requestFriend(@RequestParam Long requestUserId, @RequestParam Long responseUserId) {
        FriendResponseDto friendResponseDto = friendService.requestFriend(requestUserId, responseUserId);
        return new ResponseEntity<>(friendResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendPageResponseDto>> findMyFriends(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        List<FriendPageResponseDto> friendResponseDtoList = friendService.findMyFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<FriendPageResponseDto>> recommendFriends(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        List<FriendPageResponseDto> friendResponseDtoList = friendService.recommendFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<List<FriendPageResponseDto>> receivedFriends(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        List<FriendPageResponseDto> friendResponseDtoList = friendService.receivedFriends(userId, page, size);
        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
    }

//    @GetMapping("/sent")
//    public ResponseEntity<List<FriendResponseDto>> receivedFriends(
//            @RequestParam Long userId,
//            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
//            @RequestParam(required = false, defaultValue = "10", value = "size") int size,
//            @RequestParam(required = false, defaultValue = "updatedAt", value = "criteria") String criteria
//    ) {
//        List<FriendResponseDto> friendResponseDtoList = friendService.receivedFriends(userId, page, size, criteria);
//        return new ResponseEntity<>(friendResponseDtoList, HttpStatus.OK);
//    }
//
//    @PatchMapping("/responses/{id}")
//    public ResponseEntity<Void> requestFriend(
//            @PathVariable Long id,
//            @RequestParam int status) {
//        FriendResponseDto friendResponseDto = friendService.responseFriend(id, status);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFriend(@PathVariable Long id) {
//        FriendResponseDto friendResponseDto = friendService.deleteFriend(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



}
