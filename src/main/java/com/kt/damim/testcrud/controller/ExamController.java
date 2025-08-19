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
import java.util.UUID;

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
            @RequestParam UUID classId,
            @RequestParam(required = false) String difficulty) {
        log.info("시험 조회 요청: classId={}, difficulty={}", classId, difficulty);
        List<ExamResponse> exams = examService.findExams(classId, difficulty);
        return ResponseEntity.ok(exams);
    }
    
    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable UUID examId) {
        log.info("시험 상세 조회 요청: examId={}", examId);
        ExamResponse exam = examService.getExam(examId);
        return ResponseEntity.ok(exam);
    }
    
    @PatchMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable UUID examId,
            @Valid @RequestBody UpdateExamRequest request) {
        log.info("시험 수정 요청: examId={}, request={}", examId, request);
        ExamResponse response = examService.updateExam(examId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID examId) {
        log.info("시험 삭제 요청: examId={}", examId);
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}
