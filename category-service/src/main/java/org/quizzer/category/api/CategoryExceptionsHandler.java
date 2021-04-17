package org.quizzer.category.api;

import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CategoryExceptionsHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> categoryNotFoundExceptionHandler() {
        return ApiError.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("Category doesn't exist")
            .build()
            .toResponseEntity();
    }
}
