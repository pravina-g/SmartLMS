
package com.smartlms.backend.service;

import com.smartlms.backend.entity.*;
import com.smartlms.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final StudentTopicPerformanceRepository performanceRepository;
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(
            StudentTopicPerformanceRepository performanceRepository,
            TopicRepository topicRepository,
            LessonRepository lessonRepository,
            RecommendationRepository recommendationRepository) {

        this.performanceRepository = performanceRepository;
        this.topicRepository = topicRepository;
        this.lessonRepository = lessonRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Transactional
    public List<Recommendation> generateRecommendations(Long studentId) {

        List<Recommendation> generated = new ArrayList<>();

        List<StudentTopicPerformance> performances =
                performanceRepository.findByStudentId(studentId);

        for (StudentTopicPerformance performance : performances) {

            if (performance.getScorePercentage()
                    .compareTo(new BigDecimal("50.00")) >= 0) {
                continue;
            }

            Long topicId = performance.getTopicId();

            Topic topic = topicRepository.findById(topicId)
                    .orElse(null);

            if (topic == null) {
                continue;
            }

            Long lessonId = topic.getLessonId();

            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElse(null);

            if (lesson == null) {
                continue;
            }

            boolean alreadyExists =
                    recommendationRepository
                    .existsByStudentIdAndTopicIdAndLessonId(
                            studentId, topicId, lessonId);

            if (alreadyExists) {
                continue;
            }

            Recommendation recommendation = new Recommendation();

            recommendation.setStudentId(studentId);
            recommendation.setTopicId(topicId);
            recommendation.setLessonId(lessonId);
            recommendation.setLesson(lesson.getTitle());

            recommendation.setReason(
                    "Your score for " + topic.getName()
                    + " is " + performance.getScorePercentage()
                    + "%. Review this lesson to improve your understanding.");

            recommendation.setRecommendationTest(
                    "Review the lesson and attempt its related quiz again.");

            generated.add(
                    recommendationRepository.save(recommendation));
        }

        return generated;
    }

    public List<Recommendation> getRecommendations(Long studentId) {
        return recommendationRepository.findByStudentId(studentId);
    }
}
