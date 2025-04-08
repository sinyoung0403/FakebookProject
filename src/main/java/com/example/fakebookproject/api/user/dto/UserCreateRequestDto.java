package com.example.fakebookproject.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    // 이메일 주소
    private final String email;

    // 비밀번호
    private final String password;

    // 이름
    private final String userName;

    // 생년월일
    private final String birth;

    // 성별
    private final String gender;

    // 휴대폰 번호
    private final String phone;

}
