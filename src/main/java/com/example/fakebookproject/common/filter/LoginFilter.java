//package com.example.fakebookproject.common.filter;
//
//import com.example.fakebookproject.common.exception.CustomException;
//import com.example.fakebookproject.common.exception.ExceptionCode;
//import com.example.fakebookproject.common.exception.ExceptionResponseDto;
//import com.example.fakebookproject.common.logger.SessionLogger;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.PatternMatchUtils;
//
//import java.io.IOException;
//
//@Slf4j
//public class LoginFilter implements Filter {
//
//    private static final String[] ALLOWED_PATHS = {"/", "/api/users/signup", "/api/users/login", "/api/users/logout"};
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        String requestURI = httpRequest.getRequestURI();
//
//        // ALLOWED_PATHS에 포함된 URI로 접근시 필터 종료
//        if (isAllowedPath(requestURI)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        log.info("로그인 필터 로직 실행");
//
//        HttpSession session = httpRequest.getSession(false);
//
//        // 로그인 여부 검증
//        if (session == null || session.getAttribute("loginUser") == null) {
//            log.warn("로그인되지 않은 사용자 요청: {}", requestURI);
//
//            // 사용자 정의 예외 코드
//            ExceptionCode code = ExceptionCode.NOT_LOGGEDIN;
//
//            // 사용자 정의 예외 응답 객체
//            ExceptionResponseDto responseBody = ExceptionResponseDto.builder()
//                    .status(code.getStatus().value())
//                    .code(code.getCode())
//                    .message(code.getMessage())
//                    .build();
//
//            httpResponse.setStatus(code.getStatus().value());   // 응답 상태 코드 설정
//            httpResponse.setContentType("application/json");    // 응답 타입 설정 (JSON)
//            httpResponse.setCharacterEncoding("UTF-8");         // 응답 인코딩 설정
//
//            String json = new ObjectMapper().writeValueAsString(responseBody); // 객체를 JSON 문자열로 변환
//            httpResponse.getWriter().write(json); // 응답 바디에 JSON 문자열 작성
//
//            return;
//        }
//
//        // 세션 정보 출력
//        SessionLogger.logSessionInfo(session);
//
//        chain.doFilter(request, response);
//    }
//
//    // ALLOWED_PATHS 확인
//    private boolean isAllowedPath(String requestURI) {
//        return PatternMatchUtils.simpleMatch(ALLOWED_PATHS, requestURI);
//    }
//
//}
