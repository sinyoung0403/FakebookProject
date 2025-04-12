package com.example.fakebookproject.api.auth.controller;

import com.example.fakebookproject.api.auth.dto.TokenResponseDto;
import com.example.fakebookproject.api.auth.service.AuthService;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     *
     * @param dto 로그인 요청 정보 (이메일, 비밀번호)
     * @return accessToken 및 refreshToken 반환
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid LoginRequestDto dto) {

        // 사용자 인증 및 토큰 발급
        TokenResponseDto tokenResponseDto = authService.loginUser(dto);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenResponseDto.getAccessToken())
                .body(tokenResponseDto);
    }

    /**
     * 로그아웃
     *
     * @param authHeader 요청 헤더에서 accessToken 추출 (Authorization: Bearer ...)
     * @return 로그아웃 완료 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authHeader) {

        String accessToken = authHeader.substring(7);

        // 토큰 블랙리스트 등록
        authService.logoutUser(accessToken);

        return ResponseEntity.ok("로그아웃 완료");
    }

    /**
     * Access Token 재발급
     *
     * @param body 요청 바디에서 refreshToken 추출
     * @return 새로운 accessToken + 기존 refreshToken 반환
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        // refreshToken 유효성 검사 및 사용자 확인 후 accessToken 재발급
        String newAccessToken = authService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .body(new TokenResponseDto(newAccessToken, refreshToken));

    }

}
