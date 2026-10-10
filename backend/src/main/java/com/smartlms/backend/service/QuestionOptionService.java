

package com.smartlms.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartlms.backend.entity.QuestionOption;
import com.smartlms.backend.repository.QuestionOptionRepository;

@Service
public class QuestionOptionService {

    private final QuestionOptionRepository questionOptionRepository;

    public QuestionOptionService(
            QuestionOptionRepository questionOptionRepository) {
        this.questionOptionRepository = questionOptionRepository;
    }

    public QuestionOption saveOption(QuestionOption option) {
        return questionOptionRepository.save(option);
    }

    public List<QuestionOption> getOptionsByQuestionId(Long questionId) {
        return questionOptionRepository.findByQuestionId(questionId);
    }

    public Optional<QuestionOption> getOptionById(Long id) {
        return questionOptionRepository.findById(id);
    }

    public void deleteOption(Long id) {
        questionOptionRepository.deleteById(id);
    }
}
