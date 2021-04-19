package org.quizzer.category.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.exceptions.NameAlreadyExistsException;
import org.quizzer.category.services.CategoryService;
import org.quizzer.category.utils.mockmvc.PageResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.quizzer.category.utils.mockmvc.ErrorResultMatchers.expectError;
import static org.quizzer.category.utils.mockmvc.ErrorResultMatchers.expectFieldError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCategories_noInput_returnDefaultPagedCategories() throws Exception {
        //Given
        when(categoryService.getAll(PageRequest.of(0, 10))).then(inv -> {
            Pageable pageable = inv.getArgument(0);
            return new PageDto<>(
                List.of(
                    new CategoryDto(1L, "Math", "All yours math nightmares"),
                    new CategoryDto(2L, "Technology", "Questions about modern technology"),
                    new CategoryDto(3L, "Erotica", "Category only for mature players!")
                ),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                25,
                3
            );
        });


        //When
        ResultActions resultActions = mockMvc.perform(
            get("/category")
        );

        //Then
        PageResultMatchers.expectPage(resultActions, 0, 10, 25, 3);
        resultActions
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Math"))
            .andExpect(jsonPath("$.content[0].description").value("All yours math nightmares"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].name").value("Technology"))
            .andExpect(jsonPath("$.content[1].description").value("Questions about modern technology"))
            .andExpect(jsonPath("$.content[2].id").value(3))
            .andExpect(jsonPath("$.content[2].name").value("Erotica"))
            .andExpect(jsonPath("$.content[2].description").value("Category only for mature players!"));
    }

    @Test
    public void getCategories_withPageAndSizeParams_returnPagedCategories() throws Exception {
        //Given
        when(categoryService.getAll(any())).then(inv -> {
            Pageable pageable = inv.getArgument(0);
            return new PageDto<>(
                List.of(
                    new CategoryDto(1L, "Math", "All yours math nightmares"),
                    new CategoryDto(2L, "Technology", "Questions about modern technology"),
                    new CategoryDto(3L, "Erotica", "Category only for mature players!")
                ),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                50,
                17
            );
        });

        //When
        ResultActions resultActions = mockMvc.perform(
            get("/category?page=1&size=3")
        );

        //Then
        PageResultMatchers.expectPage(resultActions, 1, 3, 50, 17);
        resultActions
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Math"))
            .andExpect(jsonPath("$.content[0].description").value("All yours math nightmares"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].name").value("Technology"))
            .andExpect(jsonPath("$.content[1].description").value("Questions about modern technology"))
            .andExpect(jsonPath("$.content[2].id").value(3))
            .andExpect(jsonPath("$.content[2].name").value("Erotica"))
            .andExpect(jsonPath("$.content[2].description").value("Category only for mature players!"));
    }

    @Test
    public void getCategory_existingId_returnCategory() throws Exception {
        //Given
        when(categoryService.get(any())).thenAnswer(inv -> {
            Long id = inv.getArgument(0);
            return new CategoryDto(id, "Technology", "Technology category description");
        });

        //When
        ResultActions resultActions = mockMvc.perform(
            get("/category/1")
        );

        //Then
        resultActions
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Technology"))
            .andExpect(jsonPath("$.description").value("Technology category description"));
    }

    @Test
    public void getCategory_nonExistingId_returnError() throws Exception {
        //Given
        when(categoryService.get(any())).thenThrow(new CategoryNotFoundException("Category doesn't exist"));

        //When
        ResultActions resultActions = mockMvc.perform(
            get("/category/1")
        );

        //When, then
        expectError(resultActions, HttpStatus.NOT_FOUND);
    }

    @Test
    public void createCategory_tooShortName_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "",
            "description", "Long enough desc"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "name");
    }

    @Test
    public void createCategory_tooLongName_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "abcd".repeat(48) + "a",
            "description", "Long enough desc"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "name");
    }

    @Test
    public void createCategory_tooShortDescription_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "Some category name",
            "description", "sr"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "description");
    }

    @Test
    public void createCategory_tooLongDescription_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "Some category name",
            "description", "e".repeat(513)
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "description");
    }

    @Test
    public void createCategory_existingName_returnError() throws Exception {
        //Given
        when(categoryService.create(any())).thenThrow(new NameAlreadyExistsException("Name already exists"));
        Map<String, Object> body = Map.of(
            "name", "Existing name",
            "description", "Valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.CONFLICT);
    }

    @Test
    public void createCategory_validCreationRequest_returnCreatedCategory() throws Exception {
        //Given
        when(categoryService.create(any())).thenReturn(
            new CategoryDto(1L, "Some category name", "With valid desc of course!")
        );
        Map<String, Object> body = Map.of(
            "name", "Some category name",
            "description", "With valid desc of course!"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Some category name"))
            .andExpect(jsonPath("$.description").value("With valid desc of course!"));
    }

    @Test
    public void updateCategory_nonExistingCategory_returnError() throws Exception {
        //Given
        when(categoryService.update(any(), any())).thenThrow(new CategoryNotFoundException("Category doesn't exist"));
        Map<String, Object> body = Map.of(
            "name", "New valid name",
            "description", "New valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateCategory_tooShortName_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "n",
            "description", "New valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "name");
    }

    @Test
    public void updateCategory_tooLongName_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "n".repeat(193),
            "description", "New valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "name");
    }

    @Test
    public void updateCategory_tooShortDescription_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "New valid name",
            "description", "ok"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "description");
    }

    @Test
    public void updateCategory_tooLongDescription_returnError() throws Exception {
        //Given
        Map<String, Object> body = Map.of(
            "name", "New valid name",
            "description", "i".repeat(513)
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(resultActions, "description");
    }

    @Test
    public void updateCategory_existingName_returnError() throws Exception {
        //Given
        when(categoryService.update(any(), any())).thenThrow(new NameAlreadyExistsException("Name already exists"));
        Map<String, Object> body = Map.of(
            "name", "Existing name",
            "description", "Valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        expectError(resultActions, HttpStatus.CONFLICT);
    }

    @Test
    public void updateCategory_validCreationRequest_returnError() throws Exception {
        //Given
        when(categoryService.update(any(), any())).thenReturn(
            new CategoryDto(1L, "New valid name", "New valid description")
        );
        Map<String, Object> body = Map.of(
            "name", "New valid name",
            "description", "New valid description"
        );

        //When
        ResultActions resultActions = mockMvc.perform(
            put("/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );

        //Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("New valid name"))
            .andExpect(jsonPath("$.description").value("New valid description"));
    }


}