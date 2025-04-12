package com.example.fakebookproject.common.filter;

import com.example.fakebookproject.api.auth.service.BlackListService;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;

    // 인증 없이 접근을 허용할 URI 목록
    private static final String[] ALLOWED_PATHS = {"/", "/api/users/signup", "/api/auth/*"};

    /**
     * 요청마다 한 번 실행되는 JWT 인증 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // 현재 요청 URI
        String requestURI = request.getRequestURI();

        // 허용된 경로인 경우(회원가입, 로그인 등) -> 인증 필터를 건너뜀
        if (isAllowedPath(requestURI)) {
            log.debug("인증 없이 접근 허용된 URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // HTTP 헤더에서 Authorization 값 추출
        String header = request.getHeader("Authorization");

        // Authorization 헤더가 존재하지 않음
        if (header == null) {
            log.debug("Authorization 헤더 누락. URI: {}", requestURI);
            throw new CustomException(ExceptionCode.NOT_LOGGED_IN);
        }

        // "Bearer " 형식이 아닐 경우
        if (!header.startsWith("Bearer ")) {
            log.debug("Authorization 헤더 형식 오류. 헤더: {}", header);
            throw new CustomException(ExceptionCode.INVALID_AUTH_HEADER);
        }

        String token = header.substring(7);

        // 유효하지 않거나 블랙리스트에 있으면 인증 실패 처리
        if (!jwtProvider.validate(token)) {
            log.debug("JWT 유효성 검증 실패. URI: {}, token: {}", requestURI, token);
            throw new CustomException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }

        if (blackListService.isAccessTokenBlacklisted(token)) {
            log.debug("AccessToken 블랙리스트 등록됨. URI: {}, token: {}", requestURI, token);
            throw new CustomException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }

        if (blackListService.isRefreshTokenBlacklisted(token)) {
            log.debug("RefreshToken 블랙리스트 등록됨. URI: {}, token: {}", requestURI, token);
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 토큰에서 userId 추출 후 request 속성에 저장
        Long userId = jwtProvider.getUserId(token);
        log.debug("인증된 사용자 요청. userId: {}", userId);
        request.setAttribute("userId", userId);
        filterChain.doFilter(request, response);

        }

    /**
     * 허용된 경로인지 확인 (회원가입, 로그인 등)
     */
    private boolean isAllowedPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(ALLOWED_PATHS, requestURI);
    }

}
