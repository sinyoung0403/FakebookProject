package com.example.fakebookproject.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 페이징 응답 공통 DTO
 * @param <T> 응답 content로 포함될 객체 타입
 */
@Getter
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 현재 페이지에 포함될 데이터 목록
     */
    private List<T> content;

    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    private int pageNumber;

    /**
     * 한 페이지에 포함될 항목 개수
     */
    private int pageSize;

    /**
     * 전체 데이터 개수
     */
    private long totalElements;

    /**
     * 전체 페이지 개수
     */
    private int totalPages;
}