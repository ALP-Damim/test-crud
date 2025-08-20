package com.kt.damim.testcrud.repo;

import com.kt.damim.testcrud.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByExamIdOrderByPositionAsc(Long examId);
    
    void deleteByExamId(Long examId);
}
