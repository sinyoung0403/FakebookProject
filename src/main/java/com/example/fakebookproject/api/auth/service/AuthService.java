package com.example.fakebookproject.api.auth.service;

import com.example.fakebookproject.api.auth.dto.TokenResponseDto;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.config.PasswordEncoder;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BlackListService blackListService;

    /**
     * 로그인 요청 정보를 기반으로 사용자 인증 수행
     *
     * @param dto 로그인 요청 정보 (이메일, 비밀번호)
     * @return 발급된 토큰
     * @throws CustomException 이메일 미존재 또는 비밀번호 불일치 시 예외 발생 (LOGIN_FAILED)
     */
    @Transactional
    public TokenResponseDto loginUser(LoginRequestDto dto) {

        // 1. 이메일로 사용자 조회
        User user = userRepository.findUserByEmailOrElseThrow(dto.getEmail());

        // 2. 비밀번호 일치 여부 확인
        if  (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        // 3. 토큰 발급
        String accessToken  = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // 4. refreshToken DB 저장
        user.updateRefreshToken(refreshToken);

        // 5. 토큰 반환
        return new TokenResponseDto(accessToken, refreshToken);
    }

    /**
     * 로그아웃 시 Refresh Token 제거 + Token 블랙리스트 등록
     *
     * @param accessToken 클라이언트가 사용 중인 accessToken
     * @throws CustomException 리프레시 토큰이 유효하지 않거나 사용자 조회에 실패할 경우
     */
    @Transactional
    public void logoutUser(String accessToken) {

        // 1. 액세스 토큰 유효성 검사
        if (!jwtProvider.validate(accessToken)) {
            throw new CustomException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }

        // 2. 사용자 ID 추출 및 사용자 조회
        Long userId = jwtProvider.getUserId(accessToken);
        User user = userRepository.findUserByIdOrElseThrow(userId);

        // 3. DB에 저장된 리프레시 토큰
        String refreshToken = user.getRefreshToken();

        // 4. 액세스 토큰 블랙리스트 등록
        blackListService.addAccessTokenToBlacklist(accessToken);

        // 5. 리프레시 토큰 블랙리스트 등록
        blackListService.addRefreshTokenToBlacklist(refreshToken);

        // 6. 사용자 엔티티에서 리프레시 토큰 제거
        user.deleteRefreshToken();

    }

    /**
     * Access Token 재발급
     *
     * @param refreshToken 클라이언트로부터 전달받은 refreshToken
     * @return 새롭게 발급된 accessToken
     */
    public String reissueAccessToken(String refreshToken) {

        // 1. 리프레시 토큰 유효성 검사
        if (!jwtProvider.validate(refreshToken)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 블랙리스트 여부 확인
        if (blackListService.isRefreshTokenBlacklisted(refreshToken)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 3. 토큰에서 사용자 ID 추출
        Long userId = jwtProvider.getUserId(refreshToken);

        // 4. 사용자 조회
        User user = userRepository.findUserByIdOrElseThrow(userId);

        // 5. DB에 저장된 리프레시 토큰과 일치 여부 확인
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 6. 새 Access Token 발급
        return jwtProvider.createAccessToken(userId);
    }

    
}
