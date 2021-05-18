package org.quizzer.category.utils.resultmatchers;

import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
class MockMvcResponseFacade implements HttpResponse {
    private final MvcResult result;

    @Override
    public String getContent() {
        try {
            return result.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getStatusCode() {
        return result.getResponse().getStatus();
    }
}
