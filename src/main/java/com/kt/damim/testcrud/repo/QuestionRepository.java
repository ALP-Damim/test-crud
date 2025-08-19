package com.kt.damim.testcrud.repo;

import com.kt.damim.testcrud.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    
    List<Question> findByExamIdOrderByPositionAsc(UUID examId);
    
    void deleteByExamId(UUID examId);
}
