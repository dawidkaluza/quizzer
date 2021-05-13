package org.quizzer.category.dto.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.quizzer.category.domain.Category;
import org.quizzer.category.domain.CategoryStub;
import org.springframework.data.util.Pair;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryUpdateEntityMapperTest {
    private CategoryUpdateEntityMapper.Request mapper;

    @BeforeEach
    public void beforeEach() {
        mapper = new CategoryUpdateEntityMapper.Request();
    }

    @ParameterizedTest
    @MethodSource("provideTestArgs")
    public void toDto_someCategoryAndItsUpdateRequest_returnUpdatedCategory(Pair<Category, CategoryUpdateDto.Request> providedPair, Category expectedCategory) {
        // Given, when
        Category producedCategory = mapper.toEntity(providedPair);

        // Then
        assertEquals(expectedCategory, producedCategory);
        assertEquals(expectedCategory.getId(), producedCategory.getId());
        assertEquals(expectedCategory.getName(), producedCategory.getName());
        assertEquals(expectedCategory.getDescription(), producedCategory.getDescription());
    }

    private static Stream<Arguments> provideTestArgs() {
        return Stream.of(
            Arguments.of(
                Pair.of(
                    CategoryStub.of(1L, "Michael", "Jordan"),
                    new CategoryUpdateDto.Request("David", "Jordan")
                ),
                CategoryStub.of(1L, "David", "Jordan")
            ),
            Arguments.of(
                Pair.of(
                    CategoryStub.of(2L, "David", "Beckham"),
                    new CategoryUpdateDto.Request("David", "Jordan")
                ),
                CategoryStub.of(2L, "David", "Jordan")
            ),
            Arguments.of(
                Pair.of(
                    CategoryStub.of(3L, "De", "Ba"),
                    new CategoryUpdateDto.Request("De", "Ba")
                ),
                CategoryStub.of(3L, "De", "Ba")
            )
        );
    }
}