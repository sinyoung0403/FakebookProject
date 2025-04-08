package com.example.fakebookproject.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {

    // 비밀번호
    private final String password;

    // 이름
    private final String userName;

    // 생년월일
    private final String birth;

    // 휴대폰 번호
    private final String phone;

    // 이미지 주소
    private final String imageUrl;

    // 취미
    private final String hobby;

    // 지역
    private final String cityName;

}
