package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.CourseModule;

public interface CourseModuleRepository
        extends JpaRepository<CourseModule, Long> {

    List<CourseModule> findByCourseIdOrderByModuleOrderAsc(Long courseId);
}