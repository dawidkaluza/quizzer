package org.quizzer.category.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.services.CategoryService;
import org.quizzer.category.utils.mockmvc.PageResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

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
}