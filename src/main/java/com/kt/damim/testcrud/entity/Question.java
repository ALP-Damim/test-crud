package com.kt.damim.testcrud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "qtype", nullable = false)
    private QuestionType qtype;
    
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;
    
    @Column(name = "choices", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> choices;
    
    @Column(name = "answer_key", nullable = false)
    private String answerKey;
    
    @Column(name = "points", precision = 6, scale = 2)
    private BigDecimal points;
    
    @Column(name = "position", nullable = false)
    private Integer position;
    
    @Column(name = "case_insensitive", nullable = false)
    private Boolean caseInsensitive = true;
}
