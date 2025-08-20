package com.kt.damim.testcrud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Enumerated(EnumType.STRING)
    @Column(name = "qtype", nullable = false)
    private QuestionType qtype;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "choices", columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> choices;

    @Column(name = "answer_key")
    private String answerKey;

    @Column(name = "points", nullable = false)
    private BigDecimal points = BigDecimal.ZERO;

    @Column(name = "position", nullable = false)
    private int position;
}