package com.kt.damim.testcrud.dto;

import com.kt.damim.testcrud.entity.QuestionType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UpdateQuestionRequest(
    QuestionType qtype,
    String body,
    List<String> choices,
    String answerKey,
    BigDecimal points,
    @Positive(message = "문제 순서는 양수여야 합니다")
    Integer position
) {}
