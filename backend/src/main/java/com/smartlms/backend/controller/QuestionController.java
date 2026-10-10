
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

import com.smartlms.backend.entity.Question;
import com.smartlms.backend.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuiz(
            @PathVariable Long quizId) {

        return ResponseEntity.ok(
                questionService.getQuestionsByQuizId(quizId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(
            @PathVariable Long id) {

        Question question = questionService
                .getQuestionById(id)
                .orElse(null);

        if (question == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Question not found");
        }

        return ResponseEntity.ok(question);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Question> createQuestion(
            @RequestBody Question question) {

        Question savedQuestion =
                questionService.saveQuestion(question);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedQuestion);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteQuestion(
            @PathVariable Long id) {

        if (questionService.getQuestionById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Question not found");
        }

        questionService.deleteQuestion(id);

        return ResponseEntity.ok(
                "Question deleted successfully"
        );
    }
}

