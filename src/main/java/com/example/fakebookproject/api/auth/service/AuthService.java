package com.example.fakebookproject.api.auth.service;

import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;

public interface AuthService {

    User loginUser(LoginRequestDto dto);

    void logoutUser(String accessToken, String refreshToken);

    String reissueAccessToken(String refreshToken);

    void updateRefreshToken(Long userId, String refreshToken);

    boolean isAccessTokenBlacklisted(String accessToken);

    boolean isRefreshTokenBlacklisted(String refreshToken);

}
