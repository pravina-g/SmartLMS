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

import com.smartlms.backend.entity.Course;
import com.smartlms.backend.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Any authenticated user can view all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {

        return ResponseEntity.ok(
                courseService.getAllCourses()
        );
    }

    // Any authenticated user can view a single course
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(
            @PathVariable Long id) {

        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("Course not found")
                );
    }

    // Only INSTRUCTOR and ADMIN can create courses
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Course> createCourse(
            @RequestBody Course course) {

        Course savedCourse = courseService.saveCourse(course);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCourse);
    }

    // Only INSTRUCTOR and ADMIN can delete courses
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long id) {

        if (courseService.getCourseById(id).isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Course not found");
        }

        courseService.deleteCourse(id);

        return ResponseEntity.ok(
                "Course deleted successfully"
        );
    }
}