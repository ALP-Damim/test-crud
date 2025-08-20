package com.kt.damim.testcrud.service;

import com.kt.damim.testcrud.dto.*;
import com.kt.damim.testcrud.entity.Exam;
import com.kt.damim.testcrud.entity.Question;
import com.kt.damim.testcrud.exception.ExamNotFoundException;
import com.kt.damim.testcrud.exception.QuestionNotFoundException;
import com.kt.damim.testcrud.repo.ExamRepository;
import com.kt.damim.testcrud.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    
    public QuestionResponse addQuestion(Long examId, CreateQuestionRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId));
        
        Question question = new Question();
        question.setExam(exam);
        question.setQtype(request.qtype());
        question.setBody(request.body());
        question.setChoices(request.choices());
        question.setAnswerKey(request.answerKey());
        question.setPoints(request.points());
        question.setPosition(request.position());
        
        Question savedQuestion = questionRepository.save(question);
        
        return mapToQuestionResponse(savedQuestion);
    }
    
    public List<QuestionResponse> listQuestions(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId);
        }
        
        List<Question> questions = questionRepository.findByExamIdOrderByPositionAsc(examId);
        return questions.stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }
    
    public QuestionResponse updateQuestion(Long questionId, UpdateQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("문항을 찾을 수 없습니다: " + questionId));
        
        if (request.qtype() != null) {
            question.setQtype(request.qtype());
        }
        if (request.body() != null) {
            question.setBody(request.body());
        }
        if (request.choices() != null) {
            question.setChoices(request.choices());
        }
        if (request.answerKey() != null) {
            question.setAnswerKey(request.answerKey());
        }
        if (request.points() != null) {
            question.setPoints(request.points());
        }
        if (request.position() != null) {
            question.setPosition(request.position());
        }
        
        Question updatedQuestion = questionRepository.save(question);
        return mapToQuestionResponse(updatedQuestion);
    }
    
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new QuestionNotFoundException("문항을 찾을 수 없습니다: " + questionId);
        }
        
        questionRepository.deleteById(questionId);
    }
    
    public void reorderQuestions(Long examId, Map<Long, Integer> questionPositions) {
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException("시험을 찾을 수 없습니다: " + examId);
        }
        
        List<Question> questions = questionRepository.findByExamIdOrderByPositionAsc(examId);
        
        for (Question question : questions) {
            Integer newPosition = questionPositions.get(question.getId());
            if (newPosition != null) {
                question.setPosition(newPosition);
            }
        }
        
        questionRepository.saveAll(questions);
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
