package org.quizzer.question.api;

import lombok.RequiredArgsConstructor;
import org.quizzer.question.dto.base.QuestionDto;
import org.quizzer.question.dto.creation.QuestionCreationDto;
import org.quizzer.question.dto.page.PageDto;
import org.quizzer.question.dto.update.QuestionUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    @GetMapping
    public PageDto<QuestionDto> getQuestions(@PageableDefault Pageable pageable, @RequestParam(name = "content", required = false) String content) {
        return new PageDto<>(Collections.emptyList(), 0, 0, 0, 0);
    }

    @GetMapping("/{id}")
    public QuestionDto getQuestion(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDto createQuestion(@RequestBody QuestionCreationDto.Request creationRequest) {
        return null;
    }

    @PutMapping("/{id}")
    public QuestionDto updateQuestion(@PathVariable("id") Long id, @RequestBody QuestionUpdateDto.Request updateRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") Long id) {

    }
}
