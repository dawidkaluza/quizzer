package org.quizzer.question.services;

import org.quizzer.question.dto.base.QuestionDto;
import org.quizzer.question.dto.page.PageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface QuestionService {
    /**
     * Return all question that matches given content if it's not null
     * @param pageable
     * @param content
     * @return
     */
    PageDto<QuestionDto> getAllQuestions(Pageable pageable, @Nullable String content);
}
