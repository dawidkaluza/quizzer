package org.quizzer.question.services;

import org.quizzer.question.dto.base.QuestionDto;
import org.quizzer.question.dto.page.PageDto;
import org.quizzer.question.exceptions.QuestionNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface QuestionService {
    /**
     * Return all question that matches given content if it's not null
     * @param pageable page details
     * @param content string that need to be in the searched questions if it's not null.
     *                If this param is null, content is not checked.
     * @return page with questions
     */
    PageDto<QuestionDto> getAllQuestions(Pageable pageable, @Nullable String content);

    /**
     * Return question with a given id
     * @param id id that you're looking for
     * @return question that has a given id
     * @throws QuestionNotFoundException if question with a given id doesn't exist
     */
    QuestionDto getQuestion(Long id) throws QuestionNotFoundException;
}
