
package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCourseId(Long courseId);
}

