package org.quizzer.question.services;

import org.quizzer.question.dto.base.QuestionDto;
import org.quizzer.question.dto.creation.QuestionCreationDto;
import org.quizzer.question.dto.page.PageDto;
import org.quizzer.question.exceptions.CategoryNotFoundException;
import org.quizzer.question.exceptions.QuestionNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class QuestionServiceImpl implements QuestionService {
    @Override
    @Transactional(readOnly = true)
    public PageDto<QuestionDto> getAllQuestions(Pageable pageable, @Nullable String content) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDto getQuestion(Long id) throws QuestionNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public QuestionDto createQuestion(QuestionCreationDto creationRequest) throws CategoryNotFoundException {
        throw new UnsupportedOperationException();
    }
}
