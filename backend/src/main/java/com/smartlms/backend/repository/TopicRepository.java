package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.Topic;

public interface TopicRepository
        extends JpaRepository<Topic, Long> {

    List<Topic> findByLessonId(Long lessonId);
}
