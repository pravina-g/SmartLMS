
package com.smartlms.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_topic_performance")
public class StudentTopicPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions = 0;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers = 0;

    @Column(name = "score_percentage", nullable = false)
    private BigDecimal scorePercentage = BigDecimal.ZERO;

    @Column(name = "performance_status", nullable = false)
    private String performanceStatus = "WEAK";

    @Column(name = "last_updated",
            insertable = false, updatable = false)
    private LocalDateTime lastUpdated;

    public StudentTopicPerformance() {}

    public Long getId() { return id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public BigDecimal getScorePercentage() {
        return scorePercentage;
    }
    public void setScorePercentage(BigDecimal scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public String getPerformanceStatus() {
        return performanceStatus;
    }
    public void setPerformanceStatus(String performanceStatus) {
        this.performanceStatus = performanceStatus;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
