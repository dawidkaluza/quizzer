package org.quizzer.category.utils.resultmatchers;

public interface HttpResponse {
    /**
     * Return response's content
     * @return response's content
     */
    String getContent();

    /**
     * Return response's status code
     * @return response's status code
     */
    int getStatusCode();
}
