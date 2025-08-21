package com.kt.damim.testcrud.controller;

import com.kt.damim.testcrud.dto.*;
import com.kt.damim.testcrud.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    
    private final QuestionService questionService;
    
//    @PostMapping("/exams/{examId}/questions")
//    public ResponseEntity<QuestionResponse> addQuestion(
//            @PathVariable UUID examId,
//            @Valid @RequestBody CreateQuestionRequest request) {
//        log.info("문항 추가 요청: examId={}, request={}", examId, request);
//        QuestionResponse response = questionService.addQuestion(examId, request);
//        return ResponseEntity.ok(response);
//    }

    /** 문항 추가
     * @param examId 시험 ID
     * @param req 문항 생성 요청 데이터
     * @return 생성된 문항 정보
     */
    @PostMapping(value = "/exams/{examId}/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long examId,
            @Valid @RequestBody CreateQuestionRequest req) {

        QuestionResponse res = questionService.addQuestion(examId, req);
        // 201 Created + Location 헤더(선택)
        return ResponseEntity
                .created(URI.create("/api/questions/" + res.id()))
                .body(res);
    }
    
    /** 문항 목록 조회
     * @param examId 시험 ID
     * @return 문항 목록
     */
    @GetMapping("/exams/{examId}/questions")
    public ResponseEntity<List<QuestionResponse>> listQuestions(@PathVariable Long examId) {
        log.info("문항 목록 조회 요청: examId={}", examId);
        List<QuestionResponse> questions = questionService.listQuestions(examId);
        return ResponseEntity.ok(questions);
    }
    
    /** 문항 수정
     * @param questionId 문항 ID
     * @param request 문항 수정 요청 데이터
     * @return 수정된 문항 정보
     */
    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody UpdateQuestionRequest request) {
        log.info("문항 수정 요청: questionId={}, request={}", questionId, request);
        QuestionResponse response = questionService.updateQuestion(questionId, request);
        return ResponseEntity.ok(response);
    }
    
    /** 문항 삭제
     * @param questionId 문항 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        log.info("문항 삭제 요청: questionId={}", questionId);
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
    
    /** 문항 순서 변경
     * @param examId 시험 ID
     * @param questionPositions 문항 순서 변경 요청 데이터
     * @return 순서 변경 성공 여부
     */
    @PutMapping("/exams/{examId}/questions/reorder")
    public ResponseEntity<Void> reorderQuestions(
            @PathVariable Long examId,
            @RequestBody Map<Long, Integer> questionPositions) {
        log.info("문항 순서 변경 요청: examId={}, positions={}", examId, questionPositions);
        questionService.reorderQuestions(examId, questionPositions);
        return ResponseEntity.ok().build();
    }
}
