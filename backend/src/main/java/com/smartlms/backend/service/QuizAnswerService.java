
package com.smartlms.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.QuizAnswer;
import com.smartlms.backend.repository.QuizAnswerRepository;

@Service
public class QuizAnswerService {

    private final QuizAnswerRepository quizAnswerRepository;

    public QuizAnswerService(QuizAnswerRepository quizAnswerRepository) {
        this.quizAnswerRepository = quizAnswerRepository;
    }

    public QuizAnswer saveAnswer(QuizAnswer answer) {
        return quizAnswerRepository.save(answer);
    }

    public List<QuizAnswer> getAnswersByAttemptId(Long attemptId) {
        return quizAnswerRepository.findByAttemptId(attemptId);
    }
}
