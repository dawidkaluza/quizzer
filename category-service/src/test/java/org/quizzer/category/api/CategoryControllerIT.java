package org.quizzer.category.api;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CategoryControllerIT {
    @Test
    public void getCategories_noPaginationDefined_returnPagedResponse() {
        // Given
        RequestSpecification requestSpecification = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        Response response = requestSpecification
            .get("/category");

        // Then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("content.id", hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void getCategories_paginationOutOfScope_returnEmptyPage() {
    }

    @Test
    public void getCategories_validPagination_returnPagedResponse() {

    }

    @Test
    public void getCategory_nonExistingId_returnError() {

    }

    @Test
    public void getCategory_existingId_returnRelevantCategory() {

    }

    @Test
    public void createCategory_noName_returnError() {

    }

    @Test
    public void createCategory_emptyName_returnError() {

    }

    @Test
    public void createCategory_tooShortName_returnError() {

    }

    @Test
    public void createCategory_tooLongName_returnError() {

    }

    @Test
    public void createCategory_existingName_returnError() {

    }

    @Test
    public void createCategory_noDesc_returnError() {

    }

    @Test
    public void createCategory_emptyDesc_returnError() {

    }

    @Test
    public void createCategory_tooShortDesc_returnError() {

    }

    @Test
    public void createCategory_tooLongDesc_returnError() {

    }

    @Test
    public void createCategory_validRequest_returnCreatedCategory() {

    }
}
