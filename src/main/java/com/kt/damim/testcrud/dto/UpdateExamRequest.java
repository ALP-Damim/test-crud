package com.kt.damim.testcrud.dto;

import lombok.Builder;

@Builder
public record UpdateExamRequest(
    String name,
    String difficulty,
    Boolean isReady
) {}
