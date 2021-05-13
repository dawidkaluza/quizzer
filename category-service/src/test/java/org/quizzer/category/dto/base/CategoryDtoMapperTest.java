package org.quizzer.category.dto.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.quizzer.category.domain.Category;
import org.quizzer.category.domain.CategoryStub;

import java.util.stream.Stream;

public class CategoryDtoMapperTest {
    private CategoryDtoMapper categoryDtoMapper;

    @BeforeEach
    public void setUp() {
        categoryDtoMapper = new CategoryDtoMapper();
    }

    @ParameterizedTest
    @MethodSource("provideTestArgs")
    public void toDto_someCategory_returnExpectedDto(Category providedEntity, CategoryDto expectedDto) {
        // Given, when
        CategoryDto producedDto = categoryDtoMapper.toDto(providedEntity);

        // Then
        Assertions.assertEquals(producedDto, expectedDto);
    }

    private static Stream<Arguments> provideTestArgs() {
        return Stream.of(
            Arguments.of(
                CategoryStub.of(1L, "Michael", "Jordan"),
                new CategoryDto(1L, "Michael", "Jordan")
            ),
            Arguments.of(
                CategoryStub.of(69L, "David", "Beckham"),
                new CategoryDto(69L, "David", "Beckham")
            ),
            Arguments.of(
                CategoryStub.of(33L, "De", "Ba"),
                new CategoryDto(33L, "De", "Ba")
            )
        );
    }
}