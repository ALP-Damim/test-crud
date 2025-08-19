package com.kt.damim.testcrud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateExamRequest(
    @NotNull(message = "classId는 필수입니다")
    UUID classId,
    
    @NotBlank(message = "시험명은 필수입니다")
    String name,
    
    @NotBlank(message = "난이도는 필수입니다")
    String difficulty,
    
    UUID createdBy
) {}
