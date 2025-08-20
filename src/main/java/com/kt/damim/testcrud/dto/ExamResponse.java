package com.kt.damim.testcrud.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;


@Builder
public record ExamResponse(
    Long id,
    Long classId,
    String name,
    String difficulty,
    Boolean isReady,
    Long createdBy,
    Instant createdAt,
    List<QuestionResponse> questions
) {}
