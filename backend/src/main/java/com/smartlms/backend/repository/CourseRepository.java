package com.smartlms.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}