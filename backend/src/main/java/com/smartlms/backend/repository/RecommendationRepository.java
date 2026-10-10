

package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.Recommendation;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByStudentId(Long studentId);

    boolean existsByStudentIdAndTopicIdAndLessonId(
            Long studentId,
            Long topicId,
            Long lessonId);
}
