package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.Lesson;
import com.smartlms.backend.repository.LessonRepository;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByModuleId(Long moduleId) {
        return lessonRepository
                .findByModuleIdOrderByLessonOrderAsc(moduleId);
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
