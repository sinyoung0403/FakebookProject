package com.example.fakebookproject.api.user.dto;

import com.example.fakebookproject.api.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    // PK
    private final Long id;

    // 이메일 주소
    private final String email;

    // 이름
    private final String userName;

    // 생년월일
    private final LocalDate birth;

    // 성별
    private final String gender;

    // 휴대폰 번호
    private final String phone;

    // 이미지 주소
    private final String imageUrl;

    // 취미
    private final String hobby;

    // 지역
    private final String cityName;

    // 수정일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.phone = user.getPhone();
        this.imageUrl = user.getImageUrl();
        this.hobby = user.getHobby();
        this.cityName = user.getCityName();
        this.updatedAt = user.getUpdatedAt();
    }

}
