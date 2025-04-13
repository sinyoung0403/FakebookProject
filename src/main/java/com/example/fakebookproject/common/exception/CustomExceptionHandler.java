package com.example.fakebookproject.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 사용자 정의 예외 처리
     *
     * @param e CustomException (ExceptionCode에 정의된 예외 정보)
     * @return 예외 응답 (ExceptionCode에 해당하는 예외 코드 + 메시지)
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException e) {
        return ExceptionResponseDto.dtoResponseEntity(e.getExceptionCode());
    }

    /**
     * 유효성 검사 예외 처리
     *
     * @param e MethodArgumentNotValidException (바인딩 실패)
     * @return 예외 응답 (VALIDATION_FAILED 반환)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        ExceptionCode code = ExceptionCode.VALIDATION_FAILED;

        return ResponseEntity.status(code.getStatus().value())
                .body(ExceptionResponseDto.builder()
                        .status(code.getStatus().value())
                        .code(code.getCode())
                        .message(defaultMessage)
                        .build());
    }

    /**
     * JsonFormat 오류 처리
     *
     * @param e HttpMessageNotReadableException
     * @return Json → Java type 변환 실패 시 발생하는 예외 정보
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ExceptionResponseDto> handleJsonFormatException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        // 날짜 파싱 실패인지 확인
        if (cause instanceof InvalidFormatException invalidFormatEx) {
            String targetField = extractField(invalidFormatEx);
            String targetValue = invalidFormatEx.getValue().toString();
            String expectedType = invalidFormatEx.getTargetType().getSimpleName();

            String message = String.format("[%s] 필드는 '%s' 값을 '%s' 타입으로 변환할 수 없습니다.",
                    targetField, targetValue, expectedType);

            ExceptionCode code = ExceptionCode.INVALID_DATE_FORMAT;

            return ResponseEntity.status(code.getStatus().value())
                    .body(ExceptionResponseDto.builder()
                            .status(code.getStatus().value())
                            .code(code.getCode())
                            .message(message)
                            .build());
        }

        return ExceptionResponseDto.dtoResponseEntity(ExceptionCode.VALIDATION_FAILED);
    }

    private String extractField(InvalidFormatException ex) {
        return ex.getPath().stream()
                .findFirst()
                .map(JsonMappingException.Reference::getFieldName)
                .orElse("알 수 없음");
    }
}
