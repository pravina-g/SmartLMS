
package com.smartlms.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 800)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "pass_percentage", nullable = false)
    private BigDecimal passPercentage = new BigDecimal("50.00");

    public Quiz() {
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(BigDecimal passPercentage) {
        this.passPercentage = passPercentage;
    }
}

