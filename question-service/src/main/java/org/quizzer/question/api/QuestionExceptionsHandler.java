package org.quizzer.question.api;

import org.quizzer.question.exceptions.QuestionNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class QuestionExceptionsHandler {
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Object> questionNotFoundExceptionHandler() {
        return ApiError.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("Question doesn't exist")
            .build()
            .toResponseEntity();
    }
}
