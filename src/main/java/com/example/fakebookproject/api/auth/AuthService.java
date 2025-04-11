package com.example.fakebookproject.api.auth;

import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import com.example.fakebookproject.common.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenUtils tokenUtils;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    /**
     * Refresh Token 유효성 검증 | Access Token 재발급
     *
     * @param refreshToken
     * @return
     */
    public TokenResponseDto reissue(String refreshToken) {
        if (!tokenUtils.isValidRefreshToken(refreshToken)) {
            authRepository.deleteAuthByRefreshToken(refreshToken);
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        Long userId = tokenUtils.getUserIdFromRefreshToken(refreshToken);
        User user = userRepository.findUserByIdOrElseThrow(userId);

        String newAccessToken = tokenUtils.createJwtToken(user);

        return TokenResponseDto.builder().ACCESS_TOKEN(newAccessToken).REFRESH_TOKEN(refreshToken).build();
    }


    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteExpiredRefreshTokens() {
        authRepository.deleteByRefreshTokenExpireTimeBefore(new Date());
    }
}
