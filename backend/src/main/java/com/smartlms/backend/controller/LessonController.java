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

import com.smartlms.backend.entity.Lesson;
import com.smartlms.backend.service.LessonService;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    // Any authenticated user can view lessons of a module
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Lesson>> getLessonsByModule(
            @PathVariable Long moduleId) {

        return ResponseEntity.ok(
                lessonService.getLessonsByModuleId(moduleId)
        );
    }

    // Any authenticated user can view a single lesson
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(
            @PathVariable Long id) {

        Lesson lesson = lessonService
                .getLessonById(id)
                .orElse(null);

        if (lesson == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Lesson not found");
        }

        return ResponseEntity.ok(lesson);
    }

    // Only INSTRUCTOR and ADMIN can create lessons
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Lesson> createLesson(
            @RequestBody Lesson lesson) {

        Lesson savedLesson =
                lessonService.saveLesson(lesson);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLesson);
    }

    // Only INSTRUCTOR and ADMIN can delete lessons
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteLesson(
            @PathVariable Long id) {

        if (lessonService.getLessonById(id).isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Lesson not found");
        }

        lessonService.deleteLesson(id);

        return ResponseEntity.ok(
                "Lesson deleted successfully"
        );
    }
}