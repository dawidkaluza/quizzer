package org.quizzer.category.utils.resultmatchers;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.springframework.test.web.servlet.MvcResult;

public class ResultMatcher {
    private final HttpResponse httpResponse;

    public ResultMatcher(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public static ResultMatcher ofMvcResult(MvcResult result) {
        return new ResultMatcher(
            new MockMvcResponseFacade(result)
        );
    }

    public static ResultMatcher ofRestAssuredResponse(Response response) {
        return new ResultMatcher(
            new RestAssuredResponseFacade(response)
        );
    }

    public ResultMatcher status(Matcher<Integer> matcher) {
        MatcherAssert.assertThat("Invalid status", httpResponse.getStatusCode(), matcher);
        return this;
    }

    public <T> ResultMatcher jsonPath(String path, Matcher<T> matcher) {
        JsonPath jsonPath = JsonPath.compile(path);
        T object;
        try {
            object = jsonPath.read(httpResponse.getContent());
        } catch (Exception e) {
            object = null;
        }
        MatcherAssert.assertThat("Invalid result", object, matcher);
        return this;
    }
}
