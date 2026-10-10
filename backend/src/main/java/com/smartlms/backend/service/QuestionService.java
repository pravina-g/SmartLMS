
package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.Question;
import com.smartlms.backend.repository.QuestionRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}

