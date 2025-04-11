package com.example.fakebookproject.common.filter;

import com.example.fakebookproject.api.auth.service.AuthService;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.exception.ExceptionCode;
import com.example.fakebookproject.common.exception.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final AuthService authService;

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

        // HTTP 헤더에서 Authorization 값 추출
        String header = request.getHeader("Authorization");

        // 허용된 경로인 경우(회원가입, 로그인 등) -> 인증 필터를 건너뜀
        if (isAllowedPath(requestURI)) {
            log.debug("인증 없이 접근 허용된 URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더가 존재하고, "Bearer " 형식일 경우
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // 유효하지 않거나 블랙리스트에 있으면 인증 실패 처리
            if (!jwtProvider.validate(token)) {
                log.debug("JWT 유효성 검증 실패. URI: {}, token: {}", requestURI, token);
                writeUnauthorizedResponse(response, requestURI);
                return;
            }

            if (authService.isAccessTokenBlacklisted(token)) {
                log.debug("AccessToken 블랙리스트 등록됨. URI: {}, token: {}", requestURI, token);
                writeUnauthorizedResponse(response, requestURI);
                return;
            }

            if (authService.isRefreshTokenBlacklisted(token)) {
                log.debug("RefreshToken 블랙리스트 등록됨. URI: {}, token: {}", requestURI, token);
                writeUnauthorizedResponse(response, requestURI);
                return;
            }

            // 토큰에서 userId 추출 후 request 속성에 저장
            Long userId = jwtProvider.getUserId(token);
            log.info("인증된 사용자 요청. userId: {}", userId);
            request.setAttribute("userId", userId);
            filterChain.doFilter(request, response);
        } else {
            // 토큰이 없거나 Bearer 형식이 아닌 경우 인증 실패
            log.debug("Authorization 헤더 누락 또는 형식 오류. URI: {}, 헤더: {}", requestURI, header);
            writeUnauthorizedResponse(response, requestURI);
        }


    }

    /**
     * 허용된 경로인지 확인 (회원가입, 로그인 등)
     */
    private boolean isAllowedPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(ALLOWED_PATHS, requestURI);
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String requestURI) throws IOException {
        // 인증 실패 (토큰이 없거나, 잘못되었거나, 만료된 경우)
        log.warn("로그인되지 않은 사용자 요청 차단. URI: {}", requestURI);

        // 사용자 정의 예외 코드
        ExceptionCode code = ExceptionCode.NOT_LOGGEDIN;

        // 사용자 정의 예외 응답 객체
        ExceptionResponseDto responseBody = ExceptionResponseDto.builder()
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .build();

        response.setStatus(code.getStatus().value());   // 응답 상태 코드 설정
        response.setContentType("application/json");    // 응답 타입 설정 (JSON)
        response.setCharacterEncoding("UTF-8");         // 응답 인코딩 설정

        String json = new ObjectMapper().writeValueAsString(responseBody); // 객체를 JSON 문자열로 변환

        // 응답이 아직 커밋되지 않았다면 JSON 바디를 작성함
        // (한 번 커밋된 응답은 바디 수정이 불가능하므로 예외 발생 방지 목적)
        if (!response.isCommitted()) {
            response.getWriter().write(json);
        }
    }
}
