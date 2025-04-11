package com.example.fakebookproject.common.config;

import com.example.fakebookproject.common.interceptor.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터 등록
        registry.addInterceptor(jwtTokenInterceptor) // "jwtTokenInterceptor" 를 Spring 에 등록
                .addPathPatterns("/**") // 모든 요청 경로에 대해 토큰 적용
                .excludePathPatterns("/api/users/signup", "/api/users/login","/api/users/logout","/api/auth/reissue"); // signUp 경로 제외
    }
}