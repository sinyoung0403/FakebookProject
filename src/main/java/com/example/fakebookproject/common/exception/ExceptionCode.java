package com.example.fakebookproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 예외 코드 정의 enum
 */
@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /**
     * Login
     */
    NOT_LOGGEDIN(HttpStatus.UNAUTHORIZED, "NOT_LOGGEDIN", "로그인되지 않은 사용자입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 리프레시 토큰입니다."),

    /**
     * User
     */
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 가입된 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT_FOUND_USER", "사용자가 존재하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "SAME_AS_OLD_PASSWORD", "기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."),

    /**
     * Friend
     */
    ALREADY_REQUESTED(HttpStatus.CONFLICT, "ALREADY_REQUESTED", "이미 친구 요청 중인 회원입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND_REQUEST_NOT_FOUND", "요청된 친구가 없습니다."),

    /**
     * Post
     */
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "NOT_FOUND_POST", "게시글이 존재하지 않습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "FORBIDDEN_ACCESS","접근 권한이 없습니다."),

    /**
     * Comment
     */
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "NOT_FOUND_COMMENT", "댓글이 존재하지 않습니다."),

    /**
     * Like
     */
    CANNOT_LIKE_OWN(HttpStatus.BAD_REQUEST, "CANNOT_LIKE_OWN", "본인에게 좋아요를 추가할 수 없습니다."),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST, "ALREADY_LIKE", "이미 좋아요 한 게시물 입니다."),
    LIKE_FAILED(HttpStatus.BAD_REQUEST, "LIKE_FAILED", "좋아요가 존재하지 않습니다."),
    LIKE_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "LIKE_USER_NOT_FOUND", "좋아요를 수행하려는 사용자 정보가 존재하지 않습니다."),

    /**
     * Global
     */
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS","접근할 수 없는 사용자입니다."),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED","입력 값이 유효하지 않습니다."),

    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT","올바른 날짜 형식이 아닙니다."),

    UPDATE_FAILED(HttpStatus.NOT_FOUND, "UPDATE_FAILED","데이터 변경에 실패했습니다."),

    DELETE_FAILED(HttpStatus.NOT_FOUND, "DELETE_FAILED","데이터 삭제에 실패했습니다."),

    NO_CHANGES(HttpStatus.NO_CONTENT, "NO_CHANGES","변경된 내용이 없습니다."),

    RELOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RELOAD_FAILED","데이터를 불러오는 데 실패했습니다.");

    /**
     * Http 상태 코드
     */
    private final HttpStatus status;

    /**
     * 사용자 정의 예외 코드
     */
    private final String code;

    /**
     * 예외 메시지
     */
    private final String message;
    }
