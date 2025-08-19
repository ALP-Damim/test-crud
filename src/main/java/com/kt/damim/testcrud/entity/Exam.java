package com.kt.damim.testcrud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exams")
@Getter
@Setter
public class Exam extends BaseEntity {
    
    @Column(name = "class_id", nullable = false)
    private UUID classId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "difficulty", nullable = false)
    private String difficulty;
    
    @Column(name = "created_by")
    private UUID createdBy;
    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Question> questions = new ArrayList<>();
    
    public void addQuestion(Question question) {
        questions.add(question);
        question.setExam(this);
    }
    
    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setExam(null);
    }
}
