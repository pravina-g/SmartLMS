
package com.smartlms.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String lesson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "recommendation_test",
            nullable = false, columnDefinition = "TEXT")
    private String recommendationTest;

    @Column(name = "created_at",
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;

    public Recommendation() {
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRecommendationTest() {
        return recommendationTest;
    }

    public void setRecommendationTest(String recommendationTest) {
        this.recommendationTest = recommendationTest;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

