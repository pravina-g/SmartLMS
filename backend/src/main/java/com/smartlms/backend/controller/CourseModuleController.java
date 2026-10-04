package com.smartlms.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlms.backend.entity.CourseModule;
import com.smartlms.backend.service.CourseModuleService;

@RestController
@RequestMapping("/api/modules")
public class CourseModuleController {

    private final CourseModuleService courseModuleService;

    public CourseModuleController(
            CourseModuleService courseModuleService) {

        this.courseModuleService = courseModuleService;
    }

    // Any authenticated user can view modules of a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseModule>> getModulesByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                courseModuleService.getModulesByCourseId(courseId)
        );
    }

    // Any authenticated user can view a single module
    @GetMapping("/{id}")
    public ResponseEntity<?> getModuleById(
            @PathVariable Long id) {

        CourseModule module =
                courseModuleService.getModuleById(id).orElse(null);

        if (module == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Module not found");
        }

        return ResponseEntity.ok(module);
    }

    // Only INSTRUCTOR and ADMIN can create modules
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<CourseModule> createModule(
            @RequestBody CourseModule module) {

        CourseModule savedModule =
                courseModuleService.saveModule(module);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedModule);
    }

    // Only INSTRUCTOR and ADMIN can delete modules
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteModule(
            @PathVariable Long id) {

        if (courseModuleService.getModuleById(id).isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Module not found");
        }

        courseModuleService.deleteModule(id);

        return ResponseEntity.ok(
                "Module deleted successfully"
        );
    }
}
