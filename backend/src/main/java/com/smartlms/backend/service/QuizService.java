
package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.Quiz;
import com.smartlms.backend.repository.QuizRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getQuizzesByCourseId(Long courseId) {
        return quizRepository.findByCourseId(courseId);
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}

