package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.CourseModule;
import com.smartlms.backend.repository.CourseModuleRepository;

@Service
public class CourseModuleService {

    private final CourseModuleRepository courseModuleRepository;

    public CourseModuleService(
            CourseModuleRepository courseModuleRepository) {

        this.courseModuleRepository = courseModuleRepository;
    }

    public CourseModule saveModule(CourseModule module) {
        return courseModuleRepository.save(module);
    }

    public List<CourseModule> getModulesByCourseId(Long courseId) {
        return courseModuleRepository
                .findByCourseIdOrderByModuleOrderAsc(courseId);
    }

    public Optional<CourseModule> getModuleById(Long id) {
        return courseModuleRepository.findById(id);
    }

    public void deleteModule(Long id) {
        courseModuleRepository.deleteById(id);
    }
}
