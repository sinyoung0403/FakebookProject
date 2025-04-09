package com.example.fakebookproject.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {

    // 비밀번호
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
    )
    private final String password;

    // 이름
    @Size(max = 10, message = "이름은 10자 이내로 입력해주세요.")
    private final String userName;

    // 생년월일
    @JsonFormat(pattern = "yyyyMMdd")
    private final LocalDate birth;

    // 휴대폰 번호
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "유효한 휴대폰 번호를 입력하세요.")
    private final String phone;

    // 이미지 주소
    @Size(max = 2000, message = "이미지 URL은 2000자 이내여야 합니다.")
    private final String imageUrl;

    // 취미
    @Size(max = 50, message = "취미는 50자 이내로 입력해주세요.")
    private final String hobby;

    // 지역
    @Size(max = 6, message = "지역명은 6자 이내로 입력해주세요.")
    @Pattern(
            regexp = "^(서울|인천|대전|대구|부산|광주|경기|강원|충청|전라|경상|제주)$",
            message = "유효한 지역명을 입력해주세요."
    )
    private final String cityName;

}
