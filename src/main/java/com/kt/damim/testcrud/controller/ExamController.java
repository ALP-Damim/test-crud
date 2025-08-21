package com.kt.damim.testcrud.controller;

import com.kt.damim.testcrud.dto.*;
import com.kt.damim.testcrud.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Slf4j
public class ExamController {
    
    private final ExamService examService;


    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody CreateExamRequest request) {
        log.info("시험 생성 요청: {}", request);
        ExamResponse response = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getExams(
            @RequestParam Long sessionId,
            @RequestParam(required = false) String difficulty) {
        log.info("시험 조회 요청: sessionId={}, difficulty={}", sessionId, difficulty);
        List<ExamResponse> exams = examService.findExams(sessionId, difficulty);
        return ResponseEntity.ok(exams);
    }
    
    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable Long examId) {
        log.info("시험 상세 조회 요청: examId={}", examId);
        ExamResponse exam = examService.getExam(examId);
        return ResponseEntity.ok(exam);
    }
    
    @PatchMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long examId,
            @Valid @RequestBody UpdateExamRequest request) {
        log.info("시험 수정 요청: examId={}, request={}", examId, request);
        ExamResponse response = examService.updateExam(examId, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{examId}/ready")
    public ResponseEntity<ExamResponse> updateExamReadyStatus(
            @PathVariable Long examId,
            @RequestParam Boolean isReady) {
        log.info("시험 준비 상태 변경 요청: examId={}, isReady={}", examId, isReady);
        UpdateExamRequest request = UpdateExamRequest.builder()
                .isReady(isReady)
                .build();
        ExamResponse response = examService.updateExam(examId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        log.info("시험 삭제 요청: examId={}", examId);
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}
