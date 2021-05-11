package org.quizzer.category.utils.mockmvc;

import org.hamcrest.text.MatchesPattern;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class ErrorResultMatchers {
    private static final Pattern iso8601Pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9Z+-:]+)$");

    private ErrorResultMatchers() {
    }

    public static void expectError(ResultActions resultActions, HttpStatus status) throws Exception {
        resultActions
            .andExpect(status().is(status.value()))
            .andExpect(jsonPath("$.status").value(status.value()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").value(MatchesPattern.matchesPattern(iso8601Pattern)));
    }

    public static void expectFieldError(ResultActions resultActions, String field) throws Exception {
        resultActions
            .andExpect(jsonPath("$.fieldErrors[?(@.field == '" + field + "')].field").exists())
            .andExpect(jsonPath("$.fieldErrors[?(@.field == '" + field + "')].message").exists());
    }
}
