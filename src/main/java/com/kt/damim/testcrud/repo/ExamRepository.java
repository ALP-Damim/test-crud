package com.kt.damim.testcrud.repo;

import com.kt.damim.testcrud.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findBySessionIdAndDifficultyOrderByCreatedAtDesc(Long sessionId, String difficulty);
    
    List<Exam> findBySessionIdOrderByCreatedAtDesc(Long sessionId);
}
