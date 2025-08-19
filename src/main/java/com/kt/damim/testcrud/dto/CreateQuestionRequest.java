package com.kt.damim.testcrud.dto;

import com.kt.damim.testcrud.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CreateQuestionRequest(
    @NotNull(message = "문제 유형은 필수입니다")
    QuestionType qtype,
    
    @NotBlank(message = "문제 본문은 필수입니다")
    String body,
    
    List<String> choices,
    
    @NotBlank(message = "정답은 필수입니다")
    String answerKey,
    
    BigDecimal points,
    
    @NotNull(message = "문제 순서는 필수입니다")
    @Positive(message = "문제 순서는 양수여야 합니다")
    Integer position
) {}
