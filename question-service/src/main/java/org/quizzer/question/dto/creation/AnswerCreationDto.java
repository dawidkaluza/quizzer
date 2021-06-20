package org.quizzer.question.dto.creation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreationDto {
    @NotBlank
    @Size(min = 3, max = 512)
    private String content;
}
