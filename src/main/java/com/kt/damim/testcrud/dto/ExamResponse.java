package com.kt.damim.testcrud.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ExamResponse(
    UUID id,
    UUID classId,
    String name,
    String difficulty,
    UUID createdBy,
    OffsetDateTime createdAt,
    List<QuestionResponse> questions
) {}
