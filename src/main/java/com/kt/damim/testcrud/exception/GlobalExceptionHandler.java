package com.kt.damim.testcrud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExamNotFoundException(ExamNotFoundException ex) {
        log.error("시험을 찾을 수 없음: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }
    
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        log.error("문항을 찾을 수 없음: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String message = "입력값 검증에 실패했습니다: " + errors;
        log.error("입력값 검증 실패: {}", errors);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("예상치 못한 오류 발생: ", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다");
    }
    
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                error,
                message
        );
        return new ResponseEntity<>(errorResponse, status);
    }
    
    public record ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message
    ) {}
}
