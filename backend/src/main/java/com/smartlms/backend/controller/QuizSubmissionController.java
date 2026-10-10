
package com.smartlms.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlms.backend.dto.QuizSubmissionRequest;
import com.smartlms.backend.entity.QuizAttempt;
import com.smartlms.backend.repository.UserRepository;
import com.smartlms.backend.service.QuizSubmissionService;

@RestController
@RequestMapping("/api/quiz-submissions")
public class QuizSubmissionController {

    private final QuizSubmissionService quizSubmissionService;
    private final UserRepository userRepository;

    public QuizSubmissionController(
            QuizSubmissionService quizSubmissionService,
            UserRepository userRepository) {
        this.quizSubmissionService = quizSubmissionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuiz(
            @RequestBody QuizSubmissionRequest request,
            Authentication authentication) {

        var user = userRepository
                .findByEmail(authentication.getName());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            QuizAttempt result = quizSubmissionService.submitQuiz(
                    user.get().getId(), request);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
