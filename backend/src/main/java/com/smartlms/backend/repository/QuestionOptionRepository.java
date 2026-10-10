
package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartlms.backend.entity.QuestionOption;

public interface QuestionOptionRepository
        extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestionId(Long questionId);
}

