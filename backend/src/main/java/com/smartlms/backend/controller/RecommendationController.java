
package com.smartlms.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlms.backend.entity.Recommendation;
import com.smartlms.backend.entity.User;
import com.smartlms.backend.repository.UserRepository;
import com.smartlms.backend.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationController(
            RecommendationService recommendationService,
            UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Recommendation>> generate(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        List<Recommendation> recommendations =
                recommendationService.generateRecommendations(
                        user.getId());

        return ResponseEntity.ok(recommendations);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Recommendation>> getRecommendations(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        return ResponseEntity.ok(
                recommendationService.getRecommendations(user.getId()));
    }
}
