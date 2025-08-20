package com.kt.damim.testcrud.dto;

import com.kt.damim.testcrud.entity.QuestionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


@Builder
public record QuestionResponse(
    Long id,
    QuestionType qtype,
    String body,
    List<String> choices,
    String answerKey,
    BigDecimal points,
    Integer position
) {}
