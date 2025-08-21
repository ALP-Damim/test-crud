package com.kt.damim.testcrud.controller;

import com.kt.damim.testcrud.dto.*;
import com.kt.damim.testcrud.service.ExamService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "시험 관리")
public class ExamController {
    
    private final ExamService examService;

    /** 시험 생성
     * @param request 시험 생성 요청 데이터
     * @return 생성된 시험 정보
     */
    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody CreateExamRequest request) {
        log.info("시험 생성 요청: {}", request);
        ExamResponse response = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /** 시험 목록 조회
     * @param sessionId 세션 ID
     * @param difficulty 난이도
     * @return 시험 목록
     */
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getExams(
            @RequestParam Long sessionId,
            @RequestParam(required = false) String difficulty) {
        log.info("시험 조회 요청: sessionId={}, difficulty={}", sessionId, difficulty);
        List<ExamResponse> exams = examService.findExams(sessionId, difficulty);
        return ResponseEntity.ok(exams);
    }
    
    /** 시험 상세 조회
     * @param examId 시험 ID
     * @return 시험 상세 정보
     */
    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable Long examId) {
        log.info("시험 상세 조회 요청: examId={}", examId);
        ExamResponse exam = examService.getExam(examId);
        return ResponseEntity.ok(exam);
    }
    
    /** 시험 수정
     * @param examId 시험 ID
     * @param request 시험 수정 요청 데이터
     * @return 수정된 시험 정보
     */
    @PatchMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long examId,
            @Valid @RequestBody UpdateExamRequest request) {
        log.info("시험 수정 요청: examId={}, request={}", examId, request);
        ExamResponse response = examService.updateExam(examId, request);
        return ResponseEntity.ok(response);
    }
    
    /** 시험 준비 상태 변경
     * @param examId 시험 ID
     * @param isReady 준비 상태
     * @return 수정된 시험 정보
     */
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
    
    /** 시험 삭제
     * @param examId 시험 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        log.info("시험 삭제 요청: examId={}", examId);
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}
