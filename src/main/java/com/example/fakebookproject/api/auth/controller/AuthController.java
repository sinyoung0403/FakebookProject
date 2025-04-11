package com.example.fakebookproject.api.auth.controller;

import com.example.fakebookproject.api.auth.dto.TokenResponseDto;
import com.example.fakebookproject.api.auth.service.AuthService;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.common.auth.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    private final JwtProvider jwtProvider;

    /**
     * 로그인
     *
     * @param dto 로그인 요청 정보 (이메일, 비밀번호)
     * @return accessToken 및 refreshToken 반환
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid LoginRequestDto dto) {

        // 사용자 인증
        User user = authService.loginUser(dto);

        // 토큰 발급
        String accessToken  = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // refreshToken DB 저장
        authService.updateRefreshToken(user.getId(), refreshToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(new TokenResponseDto(accessToken, refreshToken));
    }

    /**
     * 로그아웃
     *
     * @param authHeader 요청 헤더에서 accessToken 추출 (Authorization: Bearer ...)
     * @param body 요청 바디에서 refreshToken 추출
     * @return 로그아웃 완료 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody Map<String, String> body) {

        String accessToken = authHeader.substring(7);
        String refreshToken = body.get("refreshToken");

        // 토큰 블랙리스트 등록
        authService.logoutUser(accessToken, refreshToken);

        return ResponseEntity.ok("로그아웃 완료");
    }

    /**
     * Access Token 재발급
     *
     * @param body 요청 바디에서 refreshToken 추출
     * @return 새로운 accessToken + 기존 refreshToken 반환
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        // refreshToken 유효성 검사 및 사용자 확인 후 accessToken 재발급
        String newAccessToken = authService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(new TokenResponseDto(newAccessToken, refreshToken));
    }

}
