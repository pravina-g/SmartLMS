
package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.QuizAttempt;
import com.smartlms.backend.repository.QuizAttemptRepository;

@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    public QuizAttemptService(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
    }

    public QuizAttempt saveAttempt(QuizAttempt attempt) {
        return quizAttemptRepository.save(attempt);
    }

    public List<QuizAttempt> getAttemptsByStudentId(Long studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    public List<QuizAttempt> getAttemptsByQuizId(Long quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }

    public Optional<QuizAttempt> getAttemptById(Long id) {
        return quizAttemptRepository.findById(id);
    }
}
