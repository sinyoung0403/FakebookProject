package com.example.fakebookproject.common.interceptor;

import com.example.fakebookproject.common.util.TokenUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Spring 에서 HTTP 요청이 Controller 에 도달하기 전에 JWT 토큰을 검사하는 인터셉터.
 * True 로 반환될 시 요청이 진행
 * False 로 반환될 시 요청이 중단
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor { // Interceptor 구현을 위한 Spring 인터페이스

    private final TokenUtils tokenUtils;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // JWT TOKEN 호출
        log.info("JWT TOKEN 호출");
        String accessToken = request.getHeader("ACCESS_TOKEN");
        log.info("ACCESS_TOKEN :{}", accessToken);
        String refreshToken = request.getHeader("REFRESH_TOKEN");
        log.info("REFRESH_TOKEN :{}", refreshToken);

        // 호출 했는데, Access Token 이 존재할 시, tokenUtils 로 유효성 검사.
        // 유효할 시 true 반환 => 컨트롤러로 전달
        if (accessToken != null) {
            if (tokenUtils.isValidToken(accessToken)) {
                return true;
            }
        }

        // 존재하지 않거나, 유효하지 않을 경우
        response.setStatus(401);
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);
        response.setHeader("message", "Check the Token");
        return false;
    }
}
