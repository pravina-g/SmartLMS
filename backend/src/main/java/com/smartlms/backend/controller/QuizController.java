
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

import com.smartlms.backend.entity.Quiz;
import com.smartlms.backend.service.QuizService;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                quizService.getQuizzesByCourseId(courseId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {

        Quiz quiz = quizService.getQuizById(id).orElse(null);

        if (quiz == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Quiz not found");
        }

        return ResponseEntity.ok(quiz);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {

        Quiz savedQuiz = quizService.saveQuiz(quiz);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedQuiz);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {

        if (quizService.getQuizById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Quiz not found");
        }

        quizService.deleteQuiz(id);

        return ResponseEntity.ok("Quiz deleted successfully");
    }
}

