package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.Lesson;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

    List<Lesson> findByModuleIdOrderByLessonOrderAsc(Long moduleId);
}
