package org.quizzer.category.utils.resultmatchers;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class RestAssuredResponseFacade implements HttpResponse {
    private final Response response;

    @Override
    public String getContent() {
        return response.asString();
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }
}
