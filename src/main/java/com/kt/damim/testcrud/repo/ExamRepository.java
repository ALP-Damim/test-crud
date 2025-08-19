package com.kt.damim.testcrud.repo;

import com.kt.damim.testcrud.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {
    
    List<Exam> findByClassIdAndDifficultyOrderByCreatedAtDesc(UUID classId, String difficulty);
    
    List<Exam> findByClassIdOrderByCreatedAtDesc(UUID classId);
}
