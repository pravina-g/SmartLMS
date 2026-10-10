
package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.QuizAttempt;

public interface QuizAttemptRepository
        extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByStudentId(Long studentId);

    List<QuizAttempt> findByQuizId(Long quizId);
}
