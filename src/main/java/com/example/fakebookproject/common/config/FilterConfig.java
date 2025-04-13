package com.example.fakebookproject.common.config;

import com.example.fakebookproject.api.auth.service.BlackListService;
import com.example.fakebookproject.common.auth.JwtProvider;
import com.example.fakebookproject.common.filter.JwtAuthenticationExceptionFilter;
import com.example.fakebookproject.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationExceptionFilter> JwtAuthenticationExceptionFilter() {
        FilterRegistrationBean<JwtAuthenticationExceptionFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtAuthenticationExceptionFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtAuthenticationFilter(jwtProvider, blackListService));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

}
