package org.quizzer.category.utils.mockmvc;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class ErrorResultMatchers {
    private ErrorResultMatchers() {
    }

    public static void expectError(ResultActions resultActions, HttpStatus status) throws Exception {
        resultActions
            .andExpect(status().is(status.value()))
            .andExpect(jsonPath("$.status").value(status.value()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
