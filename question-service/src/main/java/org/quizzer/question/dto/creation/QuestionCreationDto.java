package org.quizzer.question.dto.creation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreationDto {
    @NotBlank
    @Size(min = 3, max = 512)
    private String content;

    @NotNull
    @Valid
    private AnswerCreationDto correctAnswer;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<AnswerCreationDto> incorrectAnswers;

    @NotNull
    private Long categoryId;
}
