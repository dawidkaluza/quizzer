package org.quizzer.category.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.quizzer.category.utils.resultmatchers.ResultMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.quizzer.category.utils.resultmatchers.ErrorResultMatchers.expectError;
import static org.quizzer.category.utils.resultmatchers.ErrorResultMatchers.expectFieldError;
import static org.quizzer.category.utils.resultmatchers.PageResultMatchers.expectPage;
import static org.quizzer.category.utils.resultmatchers.ResultMatcher.ofRestAssuredResponse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerIT {
    @Test
    @Order(1)
    public void getCategories_noPaginationDefined_returnPagedResponse() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category");

        // Then
        expectPage(ofRestAssuredResponse(response), 0, 10, 5, 1);
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(1, 2, 3, 4, 5))
            .body("content.name", hasItems("Erotic", "Math", "IT", "Games", "Books"))
            .body("content.description", hasItems(
                "Only for adults",
                "All your school nightmares...",
                "Questions which can be understand also by normal human",
                "If you have no-life, this category is probably for you",
                "Many letters from even more books"
            ));
    }

    @Test
    @Order(1)
    public void getCategories_paginationOutOfScope_returnEmptyPage() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category?page=2");

        // Then
        expectPage(ofRestAssuredResponse(response), 2, 10, 5, 1);
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("content.id", empty())
            .body("content.name", empty())
            .body("content.description", empty());
    }

    @Test
    @Order(1)
    public void getCategories_validPagination_returnPagedResponse() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category?size=3");

        // Then
        expectPage(ofRestAssuredResponse(response), 0, 3, 5, 2);
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(1, 2, 3))
            .body("content.name", hasItems("Erotic", "Math", "IT"))
            .body("content.description", hasItems(
                "Only for adults",
                "All your school nightmares...",
                "Questions which can be understand also by normal human"
            ));
    }

    @Test
    @Order(1)
    public void getCategory_nonExistingId_returnError() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category/34");

        // Then
        expectError(ofRestAssuredResponse(response), HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(1)
    public void getCategory_existingId_returnRelevantCategory() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category/2");

        // Then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(2))
            .body("name", is("Math"))
            .body("description", is("All your school nightmares..."));
    }

    @Test
    public void createCategory_noName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", "Super extra categoryyy");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "name");
    }

    @Test
    public void createCategory_emptyName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "         ");
        requestBody.put("description", "Super extra categoryyy");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "name");
    }

    @Test
    public void createCategory_tooShortName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "C4");
        requestBody.put("description", "Xhaka Laca Boom");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "name");
    }

    @Test
    public void createCategory_tooLongName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Name".repeat(48) + "e");
        requestBody.put("description", "Xhaka Laca Boom");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "name");
    }

    @Test
    public void createCategory_existingName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Math");
        requestBody.put("description", "Xhaka Laca Boom");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.CONFLICT);
        expectFieldError(matcher, "name");
    }

    @Test
    public void createCategory_noDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Religion");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "description");
    }

    @Test
    public void createCategory_emptyDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Religion");
        requestBody.put("description", "        ");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "description");
    }

    @Test
    public void createCategory_tooShortDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Religion");
        requestBody.put("description", "br");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "description");
    }

    @Test
    public void createCategory_tooLongDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Religion");
        requestBody.put("description", "Desc ".repeat(128) + "Yeah");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        ResultMatcher matcher = ofRestAssuredResponse(response);
        expectError(matcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(matcher, "description");
    }

    @Test
    public void createCategory_validRequest_returnCreatedCategory() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Religion");
        requestBody.put("description", "About faith and superstition");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .post("/category");

        // Then
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", any(Number.class))
            .body("name", is("Religion"))
            .body("description", is("About faith and superstition"));
    }

    @Test
    public void updateCategory_nonExistingId_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/34");

        // Then
        expectError(ofRestAssuredResponse(response), HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateCategory_noName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "name");
    }

    @Test
    public void updateCategory_emptyName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "     ");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "name");
    }

    @Test
    public void updateCategory_tooShortName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "TI");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "name");
    }

    @Test
    public void updateCategory_tooLongName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Info".repeat(48) + "rmation");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "name");
    }

    @Test
    public void updateCategory_existingName_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Games");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.CONFLICT);
        expectFieldError(resultMatcher, "name");
    }

    @Test
    public void updateCategory_noDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "description");
    }

    @Test
    public void updateCategory_emptyDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");
        requestBody.put("description", "    ");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "description");
    }

    @Test
    public void updateCategory_tooShortDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");
        requestBody.put("description", "IT");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "description");
    }

    @Test
    public void updateCategory_tooLongDesc_returnError() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");
        requestBody.put("description", "Info".repeat(128) + "rmation");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        ResultMatcher resultMatcher = ofRestAssuredResponse(response);
        expectError(resultMatcher, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultMatcher, "description");
    }

    @Test
    public void updateCategory_validRequest_returnUpdatedCategory() {
        // Given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Information Technology");
        requestBody.put("description", "All about computers, smartphones, etc.");

        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody);

        // When
        Response response = requestSpecification
            .put("/category/3");

        // Then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(3))
            .body("name", is("Information Technology"))
            .body("description", is("All about computers, smartphones, etc."));
    }

    @Test
    public void deleteCategory_nonExistingId_returnError() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .delete("/category/34");

        // Then
        expectError(ofRestAssuredResponse(response), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteCategory_validRequest_returnNoContent() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .delete("/category/5");

        // Then
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
