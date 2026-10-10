
package com.smartlms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartlms.backend.entity.Question;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByQuizId(Long quizId);

    @Query(value = """
        SELECT topic_id
        FROM question_topics
        WHERE question_id = :questionId
        """, nativeQuery = true)
    List<Long> findTopicIdsByQuestionId(
            @Param("questionId") Long questionId);
}

