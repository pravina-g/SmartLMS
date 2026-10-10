
package com.smartlms.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlms.backend.entity.QuestionOption;
import com.smartlms.backend.service.QuestionOptionService;

@RestController
@RequestMapping("/api/options")
public class QuestionOptionController {

    private final QuestionOptionService questionOptionService;

    public QuestionOptionController(
            QuestionOptionService questionOptionService) {
        this.questionOptionService = questionOptionService;
    }

    @GetMapping("/question/{questionId}")
    public List<QuestionOption> getOptionsByQuestionId(
            @PathVariable Long questionId) {
        return questionOptionService.getOptionsByQuestionId(questionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionOption> getOptionById(
            @PathVariable Long id) {
        return questionOptionService.getOptionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public QuestionOption createOption(
            @RequestBody QuestionOption option) {
        return questionOptionService.saveOption(option);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteOption(
            @PathVariable Long id) {
        if (questionOptionService.getOptionById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        questionOptionService.deleteOption(id);
        return ResponseEntity.ok("Option deleted successfully");
    }
}
