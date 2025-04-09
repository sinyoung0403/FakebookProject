package com.example.fakebookproject.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    // 이메일 주소
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 주소를 입력하세요.")
    @Size(max = 50)
    private final String email;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
    )
    private final String password;

    // 이름
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(max = 10, message = "이름은 10자 이내로 입력해주세요.")
    private final String userName;

    // 생년월일
    @NotBlank(message = "생년월일은 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyyMMdd")
    private final LocalDate birth;

    // 성별
    @NotBlank(message = "성별은 필수 입력값입니다.")
    @Pattern(regexp = "^[MF]$", message = "성별은 M 또는 F로 입력해주세요.")
    private final String gender;

    // 휴대폰 번호
    @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "유효한 휴대폰 번호를 입력하세요.")
    private final String phone;

}
