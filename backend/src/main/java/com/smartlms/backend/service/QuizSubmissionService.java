
package com.smartlms.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlms.backend.dto.QuizSubmissionRequest;
import com.smartlms.backend.entity.Question;
import com.smartlms.backend.entity.QuestionOption;
import com.smartlms.backend.entity.QuizAnswer;
import com.smartlms.backend.entity.QuizAttempt;
import com.smartlms.backend.entity.StudentTopicPerformance;
import com.smartlms.backend.repository.QuestionOptionRepository;
import com.smartlms.backend.repository.QuestionRepository;
import com.smartlms.backend.repository.QuizAnswerRepository;
import com.smartlms.backend.repository.QuizAttemptRepository;
import com.smartlms.backend.repository.QuizRepository;
import com.smartlms.backend.repository.StudentTopicPerformanceRepository;

@Service
public class QuizSubmissionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuizAttemptRepository attemptRepository;
    private final QuizAnswerRepository answerRepository;
    private final StudentTopicPerformanceRepository performanceRepository;

    public QuizSubmissionService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            QuestionOptionRepository optionRepository,
            QuizAttemptRepository attemptRepository,
            QuizAnswerRepository answerRepository,
            StudentTopicPerformanceRepository performanceRepository) {

        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.performanceRepository = performanceRepository;
    }

    @Transactional
    public QuizAttempt submitQuiz(
            Long studentId, QuizSubmissionRequest request) {

        if (request == null || request.getQuizId() == null) {
            throw new IllegalArgumentException("Quiz ID is required");
        }

        Long quizId = request.getQuizId();

        if (!quizRepository.existsById(quizId)) {
            throw new IllegalArgumentException("Quiz not found");
        }

        var questions = questionRepository.findByQuizId(quizId);

        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Quiz has no questions");
        }

        BigDecimal totalMarks = BigDecimal.ZERO;

        for (Question question : questions) {
            totalMarks = totalMarks.add(question.getMarks());
        }

        if (totalMarks.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Quiz total marks must be greater than zero");
        }

        Set<Long> answeredQuestions = new HashSet<>();
        Map<Long, Integer> topicTotal = new HashMap<>();
        Map<Long, Integer> topicCorrect = new HashMap<>();

        BigDecimal score = BigDecimal.ZERO;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudentId(studentId);
        attempt.setQuizId(quizId);
        attempt.setScore(BigDecimal.ZERO);
        attempt.setPercentage(BigDecimal.ZERO);

        attempt = attemptRepository.save(attempt);

        if (request.getAnswers() != null) {
            for (QuizSubmissionRequest.AnswerRequest answer
                    : request.getAnswers()) {

                if (answer == null || answer.getQuestionId() == null) {
                    throw new IllegalArgumentException(
                            "Question ID is required");
                }

                Long questionId = answer.getQuestionId();

                if (!answeredQuestions.add(questionId)) {
                    throw new IllegalArgumentException(
                            "Duplicate question in submission");
                }

                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Question not found"));

                if (!question.getQuizId().equals(quizId)) {
                    throw new IllegalArgumentException(
                            "Question does not belong to this quiz");
                }

                boolean correct = false;
                Long optionId = answer.getSelectedOptionId();

                if (optionId != null) {
                    QuestionOption option = optionRepository.findById(optionId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Option not found"));

                    if (!option.getQuestionId().equals(questionId)) {
                        throw new IllegalArgumentException(
                                "Option does not belong to this question");
                    }

                    correct = option.isCorrect();
                }

                if (correct) {
                    score = score.add(question.getMarks());
                }

                QuizAnswer savedAnswer = new QuizAnswer();
                savedAnswer.setAttemptId(attempt.getId());
                savedAnswer.setQuestionId(questionId);
                savedAnswer.setSelectedOptionId(optionId);
                savedAnswer.setCorrect(correct);
                answerRepository.save(savedAnswer);

                // Find the topics connected to this question
                var topicIds = questionRepository
                        .findTopicIdsByQuestionId(questionId);

                for (Long topicId : topicIds) {
                    topicTotal.merge(topicId, 1, Integer::sum);

                    if (correct) {
                        topicCorrect.merge(topicId, 1, Integer::sum);
                    }
                }
            }
        }

        BigDecimal percentage = score
                .multiply(new BigDecimal("100"))
                .divide(totalMarks, 2, RoundingMode.HALF_UP);

        attempt.setScore(score);
        attempt.setPercentage(percentage);
        attempt = attemptRepository.save(attempt);

        // Save or update each topic's performance
        for (Long topicId : topicTotal.keySet()) {

            int total = topicTotal.get(topicId);
            int correct = topicCorrect.getOrDefault(topicId, 0);

            StudentTopicPerformance performance =
                    performanceRepository
                            .findByStudentIdAndTopicId(studentId, topicId)
                            .orElseGet(() -> {
                                StudentTopicPerformance item =
                                        new StudentTopicPerformance();
                                item.setStudentId(studentId);
                                item.setTopicId(topicId);
                                return item;
                            });

            int updatedTotal = performance.getTotalQuestions() + total;
            int updatedCorrect = performance.getCorrectAnswers() + correct;

            BigDecimal topicPercentage = BigDecimal
                    .valueOf(updatedCorrect)
                    .multiply(new BigDecimal("100"))
                    .divide(BigDecimal.valueOf(updatedTotal),
                            2, RoundingMode.HALF_UP);

            performance.setTotalQuestions(updatedTotal);
            performance.setCorrectAnswers(updatedCorrect);
            performance.setScorePercentage(topicPercentage);

            if (topicPercentage.compareTo(
                    new BigDecimal("50.00")) < 0) {
                performance.setPerformanceStatus("WEAK");
            } else {
                performance.setPerformanceStatus("GOOD");
            }

            performanceRepository.save(performance);
        }

        return attempt;
    }
}
