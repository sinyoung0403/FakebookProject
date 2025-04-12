package com.example.fakebookproject.api.auth.service;

import com.example.fakebookproject.common.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Access Token 블랙리스트 등록
     * @param accessToken 등록할 accessToken
     */
    protected void addAccessTokenToBlacklist(String accessToken) {

        // 1. 액세스 토큰 만료 시간 계산
        long expiration = jwtProvider.getExpiration(accessToken);

        // 2. 액세스 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                "blacklist:access:" + accessToken,
                "logout",
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Refresh Token 블랙리스트 등록
     * @param refreshToken 등록할 refreshToken
     */
    protected void addRefreshTokenToBlacklist(String refreshToken) {

        // 1. 리프레시 토큰 만료 시간 계산
        long refreshExp = jwtProvider.getExpiration(refreshToken);

        // 2. 리프레시 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                "blacklist:refresh:" + refreshToken,
                "logout",
                refreshExp,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 전달된 Access Token이 블랙리스트에 등록되었는지 확인
     *
     * @param accessToken 확인할 accessToken
     * @return 블랙리스트에 등록된 경우 true, 그렇지 않으면 false
     */
    public boolean isAccessTokenBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:access:" + accessToken);
    }

    /**
     * 전달된 Refresh Token이 블랙리스트에 등록되었는지 확인
     *
     * @param refreshToken 확인할 refreshToken
     * @return 블랙리스트에 등록된 경우 true, 그렇지 않으면 false
     */
    public boolean isRefreshTokenBlacklisted(String refreshToken) {
        return redisTemplate.hasKey("blacklist:refresh:" + refreshToken);
    }

}

