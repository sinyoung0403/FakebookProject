package com.example.fakebookproject.api.auth.service;

import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.config.PasswordEncoder;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProvider.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        authService = new AuthServiceImpl(jwtProvider, passwordEncoder, userRepository);
    }

    @Test
    @DisplayName("로그인 성공 시 User 반환")
    void shouldReturnUserWhenLoginSuccessful() {

        // given
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = "hashed";
        LoginRequestDto dto = new LoginRequestDto(email, password);
        User user = new User(email, hashedPassword, null, null, null, null);

        when(userRepository.findUserByEmailOrElseThrow(email)).thenReturn(user);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

        // when
        User result = authService.loginUser(dto);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("비밀번호 불일치 시 예외 발생")
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {

        // given
        String email = "test@example.com";
        String password = "wrongPassword";
        String hashedPassword = "hashed";
        LoginRequestDto dto = new LoginRequestDto(email, password);
        User user = new User(email, hashedPassword, null, null, null, null);

        when(userRepository.findUserByEmailOrElseThrow(email)).thenReturn(user);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.loginUser(dto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ExceptionCode.LOGIN_FAILED.getMessage());
    }

    @DisplayName("리프레시 토큰 검증 실패 시 예외 발생")
    @Test
    void shouldThrowExceptionWhenRefreshTokenIsInvalid() {

        // given
        String invalidToken = "invalid";
        when(jwtProvider.validate(invalidToken)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.reissueAccessToken(invalidToken))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하면 액세스 토큰 재발급")
    void shouldReissueAccessTokenWhenRefreshTokenIsValid() {

        // given
        Long userId = 1L;
        String refreshToken = "valid-refresh";
        String newAccessToken = "new-access";
        User user = new User(refreshToken);

        when(jwtProvider.validate(refreshToken)).thenReturn(true);
        when(jwtProvider.getUserId(refreshToken)).thenReturn(userId);
        when(userRepository.findUserByIdOrElseThrow(userId)).thenReturn(user);
        when(jwtProvider.createAccessToken(userId)).thenReturn(newAccessToken);

        // when
        String result = authService.reissueAccessToken(refreshToken);

        // then
        assertThat(result).isEqualTo(newAccessToken);
    }

    @Test
    @DisplayName("DB에 저장된 리프레시 토큰과 다르면 예외 발생")
    void shouldThrowExceptionWhenRefreshTokenDoesNotMatch() {

        // given
        Long userId = 1L;
        String tokenInRequest = "invalid-refresh";
        String tokenInDB = "valid-refresh";
        User user = new User(tokenInDB);

        when(jwtProvider.validate(tokenInRequest)).thenReturn(true);
        when(jwtProvider.getUserId(tokenInRequest)).thenReturn(userId);
        when(userRepository.findUserByIdOrElseThrow(userId)).thenReturn(user);

        // when & then
        assertThatThrownBy(() -> authService.reissueAccessToken(tokenInRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @DisplayName("유효하지 않은 리프레시 토큰으로 로그아웃 시 예외 발생")
    @Test
    void shouldThrowExceptionWhenLogoutWithInvalidToken() {
        // given
        String invalidToken = "invalid";
        when(jwtProvider.validate(invalidToken)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.deleteRefreshToken(invalidToken))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰 제거")
    void shouldClearRefreshTokenOnLogout() {

        // given
        Long userId = 1L;
        String refreshToken = "valid-refresh";
        User user = spy(new User(refreshToken));

        when(jwtProvider.validate(refreshToken)).thenReturn(true);
        when(jwtProvider.getUserId(refreshToken)).thenReturn(userId);
        when(userRepository.findUserByIdOrElseThrow(userId)).thenReturn(user);

        // when
        authService.deleteRefreshToken(refreshToken);

        // then
        verify(user).updateRefreshToken(null);
    }

}
