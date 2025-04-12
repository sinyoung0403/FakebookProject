package com.example.fakebookproject.common.filter;

import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import com.example.fakebookproject.common.exception.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            log.warn("토큰 검증 실패: {}", ex.getMessage());
            String requestURI = request.getRequestURI();
            writeUnauthorizedResponse(response, requestURI, ex.getExceptionCode());
        }
    }

    private void writeUnauthorizedResponse(HttpServletResponse response,
                                           String requestURI, ExceptionCode code) throws IOException {
        // 인증 실패 (토큰이 없거나, 잘못되었거나, 만료된 경우)
        log.warn("로그인되지 않은 사용자 요청 차단. URI: {}", requestURI);

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

