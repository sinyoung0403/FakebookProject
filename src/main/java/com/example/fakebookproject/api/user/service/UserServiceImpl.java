package com.example.fakebookproject.api.user.service;

import com.example.fakebookproject.api.user.dto.UserCreateRequestDto;
import com.example.fakebookproject.api.user.dto.UserResponseDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.config.PasswordEncoder;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @param dto 회원가입 요청 데이터 (이메일, 비밀번호, 이름 등)
     * @return 생성된 사용자 정보
     */
    @Override
    public UserResponseDto createUser(UserCreateRequestDto dto) {

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // User 엔티티 생성 및 저장
        User user = new User(
                dto.getEmail(),
                encodedPassword,
                dto.getUserName(),
                parseBirth(dto.getBirth()),
                dto.getGender(),
                dto.getPhone()
        );

        return new UserResponseDto(userRepository.save(user));
    }

    /**
     *
     * @param userId 검색 대상 사용자 ID
     * @return 검색된 사용자 정보
     * @Throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생
     */
    @Override
    public UserResponseDto findUserById(Long userId) {

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        return new UserResponseDto(user);
    }

    /**
     * yyyyMMdd 형식의 생년월일 문자열을 LocalDate로 변환
     */
    private LocalDate parseBirth(String birth) {
        return LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
