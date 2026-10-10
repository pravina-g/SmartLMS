
package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.QuizAnswer;

public interface QuizAnswerRepository
        extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findByAttemptId(Long attemptId);
}

