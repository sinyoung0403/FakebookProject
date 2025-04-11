package com.example.fakebookproject.api.user.controller;

import com.example.fakebookproject.api.user.dto.*;
import com.example.fakebookproject.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     *
     * @param dto 회원가입 요청 데이터 (이메일, 비밀번호, 이름 등)
     * @return 생성된 사용자 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateRequestDto dto) {
        UserResponseDto responseDto = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 상세페이지 - 유저 단건 조회
     *
     * @param userId 조회 대상 사용자 ID
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long userId) {
        UserResponseDto responseDto = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 마이페이지 - 내 정보 조회
     *
     * @param request 토큰이 저장된 HttpRequest 객체
     * @return 로그인한 사용자 정보
     */
    @GetMapping("/mypage")
    public ResponseEntity<UserResponseDto> findUserByLoginUser(HttpServletRequest request) {
        Long loginUserId = (Long) request.getAttribute("userId");
        log.info("컨트롤러에서 받은 userId: {}", loginUserId);
        UserResponseDto responseDto = userService.findUserByLoginUserId(loginUserId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 회원 정보 수정
     *
     * @param request 토큰이 저장된 HttpRequest 객체
     * @param dto 사용자 수정 요청 데이터 (비밀번호 포함)
     * @return 수정된 사용자 정보
     */
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(HttpServletRequest request,
                                                      @RequestBody @Valid UserUpdateRequestDto dto) {
        Long loginUserId = (Long) request.getAttribute("userId");
        UserResponseDto responseDto = userService.updateUser(loginUserId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 비밀번호 수정
     *
     * @param request 토큰이 저장된 HttpRequest 객체
     * @param dto 비밀번호 수정 요청 데이터
     * @return HttpStatus.OK
     */
    @PatchMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(HttpServletRequest request,
                                               @RequestBody @Valid UpdatePasswordRequestDto dto) {
        Long loginUserId = (Long) request.getAttribute("userId");
        userService.updatePassword(loginUserId, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴 (소프트 딜리트 방식)
     * 사용자의 비밀번호를 검증한 후, 실제 삭제 대신 is_deleted를 true로 변경
     *
     * @param request 토큰이 저장된 HttpRequest 객체
     * @param dto 비밀번호 요청 데이터 (사용자 본인 확인용)
     * @return HttpStatus.NO_CONTENT
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request,
                                           @RequestBody @Valid PasswordRequestDto dto) {
        Long loginUserId = (Long) request.getAttribute("userId");
        userService.deleteUser(loginUserId, dto);
        return ResponseEntity.noContent().build();
    }

}
