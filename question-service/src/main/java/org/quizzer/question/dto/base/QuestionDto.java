package org.quizzer.question.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;

    private String content;

    private List<AnswerDto> incorrectAnswers;

    private AnswerDto correctAnswer;

    private Long categoryId;
}
