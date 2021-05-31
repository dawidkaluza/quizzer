package org.quizzer.question.api;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.quizzer.question.dto.base.AnswerDto;
import org.quizzer.question.dto.base.QuestionDto;
import org.quizzer.question.dto.page.PageDto;
import org.quizzer.question.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestionControllerTest {
    @MockBean
    private QuestionService questionService;

    @Autowired
    private WebApplicationContext webAppContext;

    @BeforeEach
    public void beforeEach() {
        webAppContextSetup(webAppContext);
    }

    @Test
    public void getQuestions_noParams_returnPaginatedQuestions() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(generateRange(1, 10, i -> i, Integer.class)))
            .body("content.content", hasItems(generateRange(1, 10, i -> generateFakeQuestionContent(i, (i - 1) / 10 + 1), String.class)))
            .body("content.incorrectAnswers.id", hasSize(greaterThan(0)))
            .body("content.incorrectAnswers.content", hasSize(greaterThan(0)))
            .body("content.correctAnswer.id", notNullValue())
            .body("content.correctAnswer.content", notNullValue());

        validatePage(validatableResponse, 0, 10, 30, 3);
    }

    @Test
    public void getQuestions_secondPageParams_returnPaginatedQuestions() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question?page=1");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(generateRange(11, 20, i -> i, Integer.class)))
            .body("content.content", hasItems(generateRange(11, 20, i -> generateFakeQuestionContent(i, (i - 1) / 10 + 1), String.class)))
            .body("content.incorrectAnswers.id", hasSize(greaterThan(0)))
            .body("content.incorrectAnswers.content", hasSize(greaterThan(0)))
            .body("content.correctAnswer.id", notNullValue())
            .body("content.correctAnswer.content", notNullValue());

        validatePage(validatableResponse, 1, 10, 30, 3);
    }

    @Test
    public void getQuestions_outOfRangePageParams_returnPaginatedQuestions() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question?page=4");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content", hasSize(0))
            .body("content.id", empty())
            .body("content.content", empty())
            .body("content.incorrectAnswers.id", empty())
            .body("content.incorrectAnswers.content", empty())
            .body("content.correctAnswer.id", empty())
            .body("content.correctAnswer.content", empty());

        validatePage(validatableResponse, 4, 10, 30, 3);
    }

    @Test
    public void getQuestions_noMatchingContentParam_returnEmptyPage() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question?content=NoMatchingParam");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content", hasSize(0))
            .body("content.id", empty())
            .body("content.content", empty())
            .body("content.incorrectAnswers.id", empty())
            .body("content.incorrectAnswers.content", empty())
            .body("content.correctAnswer.id", empty())
            .body("content.correctAnswer.content", empty());

        validatePage(validatableResponse, 0, 10, 0, 0);
    }

    @Test
    public void getQuestions_partiallyMatchingContentParam_returnPaginatedQuestions() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question?content={content}", "category #2");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content", hasSize(10))
            .body("content.id", hasItems(generateRange(11, 20, i -> i, Integer.class)))
            .body("content.content", hasItems(generateRange(11, 20, i -> generateFakeQuestionContent(i, (i - 1) / 10 + 1), String.class)))
            .body("content.incorrectAnswers.id", hasSize(greaterThan(0)))
            .body("content.incorrectAnswers.content", hasSize(greaterThan(0)))
            .body("content.correctAnswer.id", notNullValue())
            .body("content.correctAnswer.content", notNullValue());

        validatePage(validatableResponse, 0, 10, 10, 1);
    }

    @Test
    public void getQuestions_partiallyMatchingContentParamAndSecondPageParams_returnPaginatedQuestions() {
        // Given
        when(questionService.getAllQuestions(any(), any()))
            .then(buildGetAllQuestionsAnswer());

        MockMvcRequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        MockMvcResponse response = reqSpec.when()
            .get("/question?page={page}&size={size}&content={content}", 1, 5, "category #3");

        // Then
        ValidatableMockMvcResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content", hasSize(5))
            .body("content.id", hasItems(generateRange(26, 30, i -> i, Integer.class)))
            .body("content.content", hasItems(generateRange(26, 30, i -> generateFakeQuestionContent(i, (i - 1) / 10 + 1), String.class)))
            .body("content.incorrectAnswers.id", hasSize(greaterThan(0)))
            .body("content.incorrectAnswers.content", hasSize(greaterThan(0)))
            .body("content.correctAnswer.id", notNullValue())
            .body("content.correctAnswer.content", notNullValue());

        validatePage(validatableResponse, 1, 5, 10, 2);
    }

    /**
     * Create range of object with specified type and pattern
     * @param from range from
     * @param to range to
     * @param function functional interface implementation used to create object for given id (it's an id of loop from "from" to "to" params)
     * @param tClass object's class produced by function implementation
     * @param <T> type of tClass
     * @return objects created by the method
     */
    private <T> T[] generateRange(int from, int to, Function<Integer, T> function, Class<T> tClass) {
        int size = to - from + 1;
        @SuppressWarnings("unchecked")
        T[] tab = (T[]) Array.newInstance(tClass, size);
        for (int i = from, tabIndex = 0; i <= to; i++) {
            T el = function.apply(i);
            tab[tabIndex++] = el;
        }
        return tab;
    }

    private String generateFakeQuestionContent(long questionId, long categoryId) {
        return "Question #" + questionId + ", category #" + categoryId;
    }

    private String generateFakeAnswerContent(long answerId, long questionId) {
        return "Answer #" + answerId + " on question #" + questionId;
    }

    /**
     * Build mockito answer for questionService.getAllQuestions method
     * Method builds specified number of fake questions and also fake answers to them
     * It uses generateFakeQuestionContent to create question content and generateFakeAnswerContent to create answer content
     * This makes the mock to look like a real invocation
     * @return answer for mocked getAllQuestions invoking
     */
    private Answer<?> buildGetAllQuestionsAnswer() {
        return inv -> {
            Pageable pageable = inv.getArgument(0);
            String content = inv.getArgument(1);

            List<QuestionDto> questions = new ArrayList<>();
            int pageStart = pageable.getPageNumber() * pageable.getPageSize() + 1;
            int pageEnd = pageStart + pageable.getPageSize() - 1;
            int matchingQuestionsNum = 0;
            for (int questionId = 1; questionId <= 30; questionId++) {
                //TODO its possible to make it better?
                long categoryId = (questionId - 1) / 10 + 1;

                String newQuestionContent = generateFakeQuestionContent(questionId, categoryId);
                if (content != null && !newQuestionContent.contains(content)) {
                    continue;
                }

                matchingQuestionsNum ++;
                if (matchingQuestionsNum < pageStart || matchingQuestionsNum > pageEnd) {
                    continue;
                }

                QuestionDto question = new QuestionDto();
                question.setId((long) questionId);
                question.setContent(newQuestionContent);
                question.setCategoryId(categoryId);

                List<AnswerDto> incorrectAnswers = new ArrayList<>();
                int correctAnswerNum = (int) (Math.random() * 4) + 1;
                for (int j = 1; j <= 4; j++) {
                    AnswerDto answer = new AnswerDto();
                    answer.setId((long) (questionId - 1) * 4 + j);
                    answer.setContent(generateFakeAnswerContent(j, questionId));

                    if (j != correctAnswerNum) {
                        incorrectAnswers.add(answer);
                    } else {
                        question.setCorrectAnswer(answer);
                    }
                }
                question.setIncorrectAnswers(incorrectAnswers);
                questions.add(question);
            }

            return new PageDto<>(
                questions,
                pageable.getPageNumber(),
                pageable.getPageSize(), matchingQuestionsNum,
                matchingQuestionsNum / pageable.getPageSize()
            );
        };
    }

    private void validatePage(ValidatableMockMvcResponse response, int number, int size, int totalElements, int totalPages) {
        response
            .body("number", is(number))
            .body("size", is(size))
            .body("totalElements", is(totalElements))
            .body("totalPages", is(totalPages));
    }
}