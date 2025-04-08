package com.example.fakebookproject.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    /**
     * 예외 코드 (ExceptionCode enum)
     */
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
