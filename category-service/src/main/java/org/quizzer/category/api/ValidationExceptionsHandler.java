package org.quizzer.category.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ValidationExceptionsHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgNotValidExceptionHandler(WebRequest webRequest, MethodArgumentNotValidException exception) {
        ApiError.ApiErrorBuilder builder = ApiError.builder();
        List<FieldError> fieldErrors = exception.getFieldErrors();
        Locale locale = webRequest.getLocale();
        for (FieldError fieldError : fieldErrors) {
            String message;
            try {
                message = messageSource.getMessage(fieldError, locale);
            } catch (NoSuchMessageException e) {
                message = "Invalid field";
            }

            builder.fieldError(
                new ApiFieldError(fieldError.getField(), message)
            );
        }

        return builder
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .message("Invalid fields")
            .build()
            .toResponseEntity();
    }
}
