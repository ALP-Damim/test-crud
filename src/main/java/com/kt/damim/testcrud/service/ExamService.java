package com.kt.damim.testcrud.service;

import com.kt.damim.testcrud.dto.*;
import com.kt.damim.testcrud.entity.Exam;
import com.kt.damim.testcrud.entity.Question;
import com.kt.damim.testcrud.exception.ExamNotFoundException;
import com.kt.damim.testcrud.repo.ExamRepository;
import com.kt.damim.testcrud.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExamService {
    
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    
    public ExamResponse createExam(CreateExamRequest request) {
        Exam exam = new Exam();
        exam.setSessionId(request.sessionId());
        exam.setName(request.name());
        exam.setDifficulty(request.difficulty());
        exam.setIsReady(request.isReady() != null ? request.isReady() : false);
        exam.setCreatedBy(request.createdBy());
        
        Exam savedExam = examRepository.save(exam);
        return mapToExamResponse(savedExam);
    }
    
    public List<ExamResponse> findExams(Long sessionId, String difficulty) {
        List<Exam> exams;
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            exams = examRepository.findBySessionIdAndDifficultyOrderByCreatedAtDesc(sessionId, difficulty);
        } else {
            exams = examRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        }
        
        return exams.stream()
                .map(this::mapToExamResponse)
                .collect(Collectors.toList());
    }
    
    public ExamResponse getExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId));
        
        return mapToExamResponse(exam);
    }
    
    public ExamResponse updateExam(Long examId, UpdateExamRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId));
        
        if (request.name() != null) {
            exam.setName(request.name());
        }
        if (request.difficulty() != null) {
            exam.setDifficulty(request.difficulty());
        }
        if (request.isReady() != null) {
            exam.setIsReady(request.isReady());
        }
        
        Exam updatedExam = examRepository.save(exam);
        return mapToExamResponse(updatedExam);
    }
    
    public void deleteExam(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId);
        }
        
        questionRepository.deleteByExamId(examId);
        examRepository.deleteById(examId);
    }
    
    private ExamResponse mapToExamResponse(Exam exam) {
        List<QuestionResponse> questionResponses = questionRepository.findByExamIdOrderByPositionAsc(exam.getId())
                .stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
        
        return ExamResponse.builder()
                .id(exam.getId())
                .sessionId(exam.getSessionId())
                .name(exam.getName())
                .difficulty(exam.getDifficulty())
                .isReady(exam.getIsReady())
                .createdBy(exam.getCreatedBy())
                .createdAt(exam.getCreatedAt())
                .questions(questionResponses)
                .build();
    }
    
    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .qtype(question.getQtype())
                .body(question.getBody())
                .choices(question.getChoices())
                .answerKey(question.getAnswerKey())
                .points(question.getPoints())
                .position(question.getPosition())
                .build();
    }
}
