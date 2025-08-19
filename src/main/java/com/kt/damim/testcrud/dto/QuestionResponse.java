package com.kt.damim.testcrud.dto;

import com.kt.damim.testcrud.entity.QuestionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record QuestionResponse(
    UUID id,
    QuestionType qtype,
    String body,
    List<String> choices,
    String answerKey,
    BigDecimal points,
    Integer position,
    Boolean caseInsensitive,
    OffsetDateTime createdAt
) {}
