
package com.smartlms.backend.dto;

import java.util.List;

public class QuizSubmissionRequest {

    private Long quizId;
    private List<AnswerRequest> answers;

    public QuizSubmissionRequest() {
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<AnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerRequest> answers) {
        this.answers = answers;
    }

    public static class AnswerRequest {

        private Long questionId;
        private Long selectedOptionId;

        public AnswerRequest() {
        }

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getSelectedOptionId() {
            return selectedOptionId;
        }

        public void setSelectedOptionId(Long selectedOptionId) {
            this.selectedOptionId = selectedOptionId;
        }
    }
}

