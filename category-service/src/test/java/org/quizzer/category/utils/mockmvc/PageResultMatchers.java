package org.quizzer.category.utils.mockmvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public final class PageResultMatchers {
    private PageResultMatchers() {
    }

    public static void expectPage(ResultActions resultActions, int number, int size, int totalElements, int totalPages) throws Exception {
        resultActions
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.number").value(number))
            .andExpect(jsonPath("$.size").value(size))
            .andExpect(jsonPath("$.totalElements").value(totalElements))
            .andExpect(jsonPath("$.totalPages").value(totalPages));
    }
}
