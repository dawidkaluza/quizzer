package org.quizzer.question.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class QuestionControllerTest {
    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        RestAssuredMockMvc.webAppContextSetup(webAppContext);
        RestAssured.port = port;

        for (int i = 1; i <= 30; i++) {
            int categoryId = i / 10 + 1;
            jdbcTemplate.update(
                "INSERT INTO QUESTION (CONTENT, CATEGORY_ID) VALUES (?, ?)",
                "Question #" + i + ", category #" + categoryId,
                categoryId
            );

            int answersNum = (i - 1) / 10 + 2;
            int correctAnswerId = (int) (Math.random() * answersNum) + 1;
            for (int j = 1; j <= answersNum; j++) {
                jdbcTemplate.update(
                    "INSERT INTO ANSWER (CONTENT, CORRECT, QUESTION_ID) VALUES (?, ?, ?)",
                    "Answer #" + j + " on question #" + i,
                    correctAnswerId == j,
                    i
                );
            }
        }
    }

    @Test
    public void getQuestions_noParams_returnPaginatedQuestions() {
        // Given
        RequestSpecification reqSpec = given()
            .contentType(ContentType.JSON);

        // When
        Response response = reqSpec.when()
            .get("question");

        // Then
        ValidatableResponse validatableResponse = response.then();

        validatableResponse
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        expectPage(validatableResponse, 0, 10, 30, 3);
    }

    @Test
    public void getQuestions_secondPageParams_returnPaginatedQuestions() {
        // Given

        // When

        // Then
    }

    @Test
    public void getQuestions_outOfRangePageParams_returnPaginatedQuestions() {
        // Given

        // When

        // Then
    }

    @Test
    public void getQuestions_noMatchingContentParam_returnEmptyPage() {
        // Given

        // When

        // Then
    }

    @Test
    public void getQuestions_partiallyMatchingContentParam_returnPaginatedQuestions() {
        // Given

        // When

        // Then
    }

    @Test
    public void getQuestions_partiallyMatchingContentParamAndSecondPageParams_returnPaginatedQuestions() {
        // Given

        // When

        // Then
    }

    private void expectPage(ValidatableResponse response, int number, int size, int totalElements, int totalPages) {
        response
            .body("number", is(number))
            .body("number", is(size))
            .body("number", is(totalElements))
            .body("number", is(totalPages));
    }

}