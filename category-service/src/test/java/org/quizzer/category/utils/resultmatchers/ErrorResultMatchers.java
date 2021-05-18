package org.quizzer.category.utils.resultmatchers;

import org.hamcrest.text.MatchesPattern;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public final class ErrorResultMatchers {
    private static final Pattern iso8601Pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9Z+-:]+)$");

    private ErrorResultMatchers() {
    }

    public static void expectError(ResultMatcher resultMatcher, HttpStatus status) {
        resultMatcher
            .status(is(status.value()))
            .jsonPath("$.status", is(status.value()))
            .jsonPath("$.message", notNullValue())
            .jsonPath("$.timestamp", MatchesPattern.matchesPattern(iso8601Pattern));
    }

    public static void expectFieldError(ResultMatcher resultMatcher, String field) {
        resultMatcher
            .jsonPath("$.fieldErrors[?(@.field == '" + field + "')].field", notNullValue())
            .jsonPath("$.fieldErrors[?(@.field == '" + field + "')].message", notNullValue());
    }
}
