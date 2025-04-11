package com.example.fakebookproject.api.auth.service;

import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.config.PasswordEncoder;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 로그인 요청 정보를 기반으로 사용자 인증 수행
     *
     * @param dto 로그인 요청 정보 (이메일, 비밀번호)
     * @return 인증된 사용자 객체
     * @throws CustomException 이메일 미존재 또는 비밀번호 불일치 시 예외 발생 (LOGIN_FAILED)
     */
    @Override
    public User loginUser(LoginRequestDto dto) {

        // 1. 이메일로 사용자 조회
        User user = userRepository.findUserByEmailOrElseThrow(dto.getEmail());

        // 2. 비밀번호 일치 여부 확인
        if  (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        // 3. 인증 성공 시 사용자 반환
        return user;
    }

    /**
     * 로그아웃 시 Refresh Token 제거 + Token 블랙리스트 등록
     *
     * @param accessToken 클라이언트가 사용 중인 accessToken
     * @param refreshToken 클라이언트가 보유한 refreshToken
     * @throws CustomException 리프레시 토큰이 유효하지 않거나 사용자 조회에 실패할 경우
     */
    @Override
    public void logoutUser(String accessToken, String refreshToken) {

        // 1. 리프레시 토큰 유효성 검사
        if (!jwtProvider.validate(refreshToken)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 사용자 ID 추출 및 사용자 조회
        Long userId = jwtProvider.getUserId(refreshToken);
        User user = userRepository.findUserByIdOrElseThrow(userId);

        // 3. 사용자 엔티티에서 리프레시 토큰 제거
        user.updateRefreshToken(null);

        // 4. 액세스 토큰 만료 시간 계산
        long expiration = jwtProvider.getExpiration(accessToken);

        // 5. 액세스 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                "blacklist:access:" + accessToken,
                "logout",
                expiration,
                TimeUnit.MILLISECONDS
        );

        // 6. 리프레시 토큰 만료 시간 계산
        long refreshExp = jwtProvider.getExpiration(refreshToken);

        // 7. 리프레시 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                "blacklist:refresh:" + refreshToken,
                "logout",
                refreshExp,
                TimeUnit.MILLISECONDS
        );

    }

    /**
     * Access Token 재발급
     *
     * @param refreshToken 클라이언트로부터 전달받은 refreshToken
     * @return 새롭게 발급된 accessToken
     */
    @Override
    public String reissueAccessToken(String refreshToken) {

        // 1. 리프레시 토큰 유효성 검사
        if (!jwtProvider.validate(refreshToken)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 블랙리스트 여부 확인
        if (isRefreshTokenBlacklisted(refreshToken)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 2. 토큰에서 사용자 ID 추출
        Long userId = jwtProvider.getUserId(refreshToken);

        // 3. 사용자 조회
        User user = userRepository.findUserByIdOrElseThrow(userId);

        // 4. DB에 저장된 리프레시 토큰과 일치 여부 확인
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 5. 새 Access Token 발급
        return jwtProvider.createAccessToken(userId);
    }

    /**
     * Refresh Token DB 저장 (로그인 시 호출됨)
     *
     * @param userId 저장 대상 사용자 ID
     * @param refreshToken 발급한 refreshToken
     */
    @Transactional
    @Override
    public void updateRefreshToken(Long userId, String refreshToken) {

        // 1. 사용자 조회
        User user = userRepository.findUserByIdOrElseThrow(userId);

        // 2. 리프레시 토큰 DB 저장
        user.updateRefreshToken(refreshToken);
    }

    /**
     * 전달된 Access Token이 블랙리스트에 등록되었는지 확인
     *
     * @param accessToken 확인할 accessToken
     * @return 블랙리스트에 등록된 경우 true, 그렇지 않으면 false
     */
    @Override
    public boolean isAccessTokenBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:access:" + accessToken);
    }

    /**
     * 전달된 Refresh Token이 블랙리스트에 등록되었는지 확인
     *
     * @param refreshToken 확인할 refreshToken
     * @return 블랙리스트에 등록된 경우 true, 그렇지 않으면 false
     */
    @Override
    public boolean isRefreshTokenBlacklisted(String refreshToken) {
        return redisTemplate.hasKey("blacklist:refresh:" + refreshToken);
    }
    
}
