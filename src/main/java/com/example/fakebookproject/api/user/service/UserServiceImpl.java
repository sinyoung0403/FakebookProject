package com.example.fakebookproject.api.user.service;

import com.example.fakebookproject.api.user.dto.*;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.config.PasswordEncoder;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @throws CustomException 이미 존재하는 이메일인 경우 예외 발생 (DUPLICATE_EMAIL)
     */
    @Override
    public UserResponseDto createUser(UserCreateRequestDto dto) {

        // 이메일 존재 여부 확인
        userRepository.validateNotExistenceByUserEmail(dto.getEmail());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // User 엔티티 생성 및 저장
        User user = new User(dto.getEmail(), encodedPassword, dto.getUserName(),
                    dto.getBirth(), dto.getGender(), dto.getPhone());

        return new UserResponseDto(userRepository.save(user));
    }

    /**
     * 상세페이지 - 유저 단건 조회
     *
     * @param userId 조회 대상 사용자 ID
     * @return 조회된 사용자 정보
     * @throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생 (NOT_FOUND_USER)
     */
    @Override
    public UserResponseDto findUserById(Long userId) {

        // 유저 검색
        User user = userRepository.findUserByIdOrElseThrow(userId);

        return new UserResponseDto(user);
    }

    /**
     * 마이페이지 - 내 정보 조회
     *
     * @param loginUserId loginUser 세션에 저장된 로그인 사용자 ID
     * @return 로그인한 사용자 정보
     * @throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException 비밀번호 불일치 시 예외 발생 (INVALID_PASSWORD)
     */
    @Override
    public UserResponseDto findUserByLoginUserId(Long loginUserId) {

        // 유저 검색
        User user = userRepository.findUserByIdOrElseThrow(loginUserId);

        return new UserResponseDto(user);
    }

    /**
     * 회원 정보 수정
     *
     * @param loginUserId 세션에 저장된 로그인 사용자 ID
     * @param dto 사용자 수정 요청 데이터 (검증용 비밀번호 포함)
     * @return 수정된 사용자 정보
     * @throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException 비밀번호 불일치 시 예외 발생 (INVALID_PASSWORD)
     */
    @Transactional
    @Override
    public UserResponseDto updateUser(Long loginUserId, UserUpdateRequestDto dto) {

        // 유저 검색
        User user = userRepository.findUserByIdOrElseThrow(loginUserId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);
        }

        user.updateUser(dto.getUserName(), dto.getBirth(), dto.getPhone(),
                dto.getImageUrl(), dto.getHobby(), dto.getCityName());

        return new UserResponseDto(user);
    }

    /**
     * 비밀번호 수정
     *
     * @param loginUserId 세션에 저장된 로그인 사용자 ID
     * @param dto 비밀번호 수정 요청 데이터
     * @throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException 비밀번호 불일치 시 예외 발생 (INVALID_PASSWORD)
     * @throws CustomException 기존 비밀번호로 변경 시 예외 발생 (SAME_AS_OLD_PASSWORD)
     */
    @Transactional
    @Override
    public void updatePassword(Long loginUserId, UpdatePasswordRequestDto dto) {

        // 유저 검색
        User user = userRepository.findUserByIdOrElseThrow(loginUserId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);
        }

        // 기존 비밀번호와 수정 요청 비밀번호가 일치하는 지 확인 후 일치하면 예외 발생
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.SAME_AS_OLD_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        user.updatePassword(encodedPassword);
    }

    /**
     * 회원 탈퇴 (소프트 딜리트 방식)
     * 사용자의 비밀번호를 검증한 후, 실제 삭제 대신 is_deleted를 true로 변경
     *
     * @param loginUserId 세션에 저장된 로그인 사용자 ID
     * @param dto 비밀번호 요청 데이터 (사용자 본인 확인용)
     * @throws CustomException 사용자 정보가 존재하지 않을 경우 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException 비밀번호 불일치 시 예외 발생 (INVALID_PASSWORD)
     */
    @Override
    public void deleteUser(Long loginUserId, PasswordRequestDto dto) {

        // 유저 검색
        User user = userRepository.findUserByIdOrElseThrow(loginUserId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);
        }

        userRepository.delete(user);
    }

    /**
     * 로그인 요청 정보를 기반으로 사용자 인증 수행
     *
     * @param dto 로그인 요청 정보 (이메일, 비밀번호)
     * @return 인증된 사용자 객체
     * @throws CustomException 이메일 미존재 또는 비밀번호 불일치 시 예외 발생 (LOGIN_FAILED)
     */
    @Override
    public User loginUser(LoginRequestDto dto) {

        // 이메일로 사용자 조회
        User user = userRepository.findUserByEmailOrElseThrow(dto.getEmail());

        // 비밀번호가 일치하지 않으면 예외 발생
        if  (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        // 인증 성공 시 사용자 반환
        return user;
    }

}
