package com.example.fakebookproject.api.user.controller;

import com.example.fakebookproject.api.user.dto.*;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto dto) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    /**
     * 상세페이지 - 유저 단건 조회
     *
     * @param userId 조회 대상 사용자 ID
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }

    /**
     * 마이페이지 - 내 정보 조회
     *
     * @param loginUserId 세션에 저장된 로그인 사용자 ID
     * @param dto 비밀번호 요청 데이터 (사용자 본인 확인용)
     * @return 로그인한 사용자 정보
     */
    @PostMapping("/mypage")
    public ResponseEntity<UserResponseDto> findUserByLoginUser(@SessionAttribute("loginUser") Long loginUserId,
                                                               @RequestBody PasswordRequestDto dto) {
        return new ResponseEntity<>(userService.findUserByLoginUserId(loginUserId, dto), HttpStatus.OK);
    }

    /**
     * 회원 정보 수정
     *
     * @param loginUserId 세션에 저장된 로그인 사용자 ID
     * @param dto 사용자 수정 요청 데이터 (비밀번호 포함)
     * @return 수정된 사용자 정보
     */
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(@SessionAttribute("loginUser") Long loginUserId,
                                                      @RequestBody UserUpdateRequestDto dto) {
        return new ResponseEntity<>(userService.updateUser(loginUserId, dto), HttpStatus.OK);
    }

    /**
     * 로그인
     *
     * @param dto 로그인 요청 정보 (아이디, 비밀번호)
     * @param httpRequest 세션 생성에 사용
     * @return 로그인 성공 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDto dto, HttpServletRequest httpRequest) {

        User user = userService.loginUser(dto);

        // 세션이 없으면 새로 생성하고, 있으면 기존 세션을 사용
        HttpSession session = httpRequest.getSession();

        // 세션에 사용자 ID 저장
        session.setAttribute("loginUser", user.getId());

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    /**
     * 로그아웃
     *
     * @param httpRequest 세션 조회 및 무효화에 사용
     * @return 로그아웃 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest httpRequest) {

        // 세션이 존재하면 반환하고, 없으면 null 반환
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return new ResponseEntity<>("로그아웃", HttpStatus.OK);
    }

}
