
package com.smartlms.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlms.backend.entity.Lesson;
import com.smartlms.backend.entity.Recommendation;
import com.smartlms.backend.entity.StudentTopicPerformance;
import com.smartlms.backend.entity.Topic;
import com.smartlms.backend.repository.LessonRepository;
import com.smartlms.backend.repository.RecommendationRepository;
import com.smartlms.backend.repository.StudentTopicPerformanceRepository;
import com.smartlms.backend.repository.TopicRepository;

@Service
public class RecommendationService {

    private static final BigDecimal WEAK_SCORE =
            new BigDecimal("50.00");

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

            Long topicId = performance.getTopicId();
            BigDecimal score = performance.getScorePercentage();

            if (score == null) {
                continue;
            }

            Topic topic = topicRepository.findById(topicId)
                    .orElse(null);

            if (topic == null) {
                continue;
            }

            Long lessonId = topic.getLessonId();

            if (lessonId == null) {
                continue;
            }

            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElse(null);

            if (lesson == null) {
                continue;
            }

            var existing = recommendationRepository
                    .findByStudentIdAndTopicIdAndLessonId(
                            studentId, topicId, lessonId);

            if (score.compareTo(WEAK_SCORE) >= 0) {

                existing.ifPresent(recommendation -> {
                    recommendation.setCompleted(true);
                    recommendation.setReason(
                            "Your score for " + topic.getName()
                            + " is " + score
                            + "%. No further review is currently needed.");
                    recommendationRepository.save(recommendation);
                });

                continue;
            }

            if (existing.isPresent()) {

                Recommendation recommendation = existing.get();

                recommendation.setReason(
                        "Your score for " + topic.getName()
                        + " is " + score
                        + "%. Review this lesson to improve your understanding.");

                recommendation.setRecommendationTest(
                        "Review the lesson and attempt its related quiz again.");

                recommendation.setLesson(lesson.getTitle());
                recommendation.setCompleted(false);

                recommendationRepository.save(recommendation);
                continue;
            }

            Recommendation recommendation = new Recommendation();

            recommendation.setStudentId(studentId);
            recommendation.setTopicId(topicId);
            recommendation.setLessonId(lessonId);
            recommendation.setLesson(lesson.getTitle());

            recommendation.setReason(
                    "Your score for " + topic.getName()
                    + " is " + score
                    + "%. Review this lesson to improve your understanding.");

            recommendation.setRecommendationTest(
                    "Review the lesson and attempt its related quiz again.");

            generated.add(recommendationRepository.save(recommendation));
        }

        return generated;
    }

    public List<Recommendation> getRecommendations(Long studentId) {
        return recommendationRepository.findByStudentId(studentId);
    }
}
