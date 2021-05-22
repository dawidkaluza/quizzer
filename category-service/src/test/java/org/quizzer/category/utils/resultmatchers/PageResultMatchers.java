package org.quizzer.category.utils.resultmatchers;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public final class PageResultMatchers {
    private PageResultMatchers() {
    }

    public static void expectPage(ResultMatcher resultMatcher, int pageNumber, int size, int totalElements, int totalPages) {
        resultMatcher
            .jsonPath("$.content", instanceOf(List.class))
            .jsonPath("$.number", is(pageNumber))
            .jsonPath("$.size", is(size))
            .jsonPath("$.totalElements", is(totalElements))
            .jsonPath("$.totalPages", is(totalPages));
    }
}
