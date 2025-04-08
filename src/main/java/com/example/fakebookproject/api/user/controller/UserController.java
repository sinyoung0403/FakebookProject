package com.example.fakebookproject.api.user.controller;

import com.example.fakebookproject.api.user.dto.UserCreateRequestDto;
import com.example.fakebookproject.api.user.dto.UserResponseDto;
import com.example.fakebookproject.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     *
     * @param dto 회원가입 요청 데이터 (이메일, 비밀번호, 이름 등)
     * @return 생성된 사용자 정보 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto dto) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

}
