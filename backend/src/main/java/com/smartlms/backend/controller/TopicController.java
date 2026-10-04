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

import com.smartlms.backend.entity.Topic;
import com.smartlms.backend.service.TopicService;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    // Any authenticated user can view topics of a lesson
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Topic>> getTopicsByLesson(
            @PathVariable Long lessonId) {

        return ResponseEntity.ok(
                topicService.getTopicsByLessonId(lessonId)
        );
    }

    // Any authenticated user can view a single topic
    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(
            @PathVariable Long id) {

        Topic topic = topicService
                .getTopicById(id)
                .orElse(null);

        if (topic == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Topic not found");
        }

        return ResponseEntity.ok(topic);
    }

    // Only INSTRUCTOR and ADMIN can create topics
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<Topic> createTopic(
            @RequestBody Topic topic) {

        Topic savedTopic =
                topicService.saveTopic(topic);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTopic);
    }

    // Only INSTRUCTOR and ADMIN can delete topics
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteTopic(
            @PathVariable Long id) {

        if (topicService.getTopicById(id).isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Topic not found");
        }

        topicService.deleteTopic(id);

        return ResponseEntity.ok(
                "Topic deleted successfully"
        );
    }
}
