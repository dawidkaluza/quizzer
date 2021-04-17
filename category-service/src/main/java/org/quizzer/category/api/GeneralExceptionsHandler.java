package org.quizzer.category.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order
public class GeneralExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        return ApiError.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .message("Internal server error")
            .build()
            .toResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ApiError.builder()
            .status(status)
            .message(ex.getMessage())
            .headers(headers)
            .build()
            .toResponseEntity();
    }
}
